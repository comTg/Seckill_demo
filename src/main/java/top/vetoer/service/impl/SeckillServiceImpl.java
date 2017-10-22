package top.vetoer.service.impl;

import org.apache.commons.collections.MapUtils;
import top.vetoer.dao.SeckillDao;
import top.vetoer.dao.SuccessKilledDao;
import top.vetoer.dao.cache.RedisDao;
import top.vetoer.dto.Exposer;
import top.vetoer.dto.SeckillExecution;
import top.vetoer.entity.Seckill;
import top.vetoer.entity.SuccessKilled;
import top.vetoer.enums.SeckillStatEnum;
import top.vetoer.exception.RepeatKillException;
import top.vetoer.exception.SeckillCloseException;
import top.vetoer.exception.SeckillException;
import top.vetoer.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import top.vetoer.utils.SeckillUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisDao redisDao;

    // 注入service依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * 查询全部的秒杀记录
     */
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     */
    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 在秒杀开启时输出秒杀接口的地址,否则输出系统时间和秒杀时间
     * 使用注解控制事物方法的优点:
     * 1. 开发团队达成一致约定,明确标注事物方法的编程风格
     * 2. 保证事物方法的执行时间尽可能短,不要穿插其它网络操作RPC/HTTP请求或者剥离到事物方法外部
     * 3. 不是所有事物的方法都需要事物,如只有一条修改操作,只读操作不要事物控制
     *
     * @param seckillId
     */
    @Transactional
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化点:缓存优化,:超时的基础上维护一致性
        /**
         * get from cache
         * if null
         * get db
         * else
         *      put cache
         * locgoin
         */
        // 1: 访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 2: 访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {      //说明查不到这个商品的记录
                return new Exposer(false, seckillId);
            } else {
                // 3:放入redis
                redisDao.putSeckill(seckill);
            }
        }

        // 若是秒杀开启
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 秒杀开启,返回秒杀商品的id,用给接口加密的md5
        String md5 = SeckillUtils.getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 根据输入名查找指定商品
     */
    public List<Seckill> queryByName(String name){
        if(name!=null){
            // 模糊查询
            String matchName = "%"+name+"%";
            List<Seckill> seckills = seckillDao.queryByName(matchName);
            return seckills;
        }
        return null;
    }


    /**
     * 执行秒杀操作,有可能失败,有可能成功,所以我们要抛出异常
     * 秒杀是否成功,成功,减库存,增加明细,失败,抛出异常,事物回滚
     *
     * @param seckillId
     * @param userId
     * @param md5
     */
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userId, String md5) throws SeckillException, RepeatKillException, SeckillException {
        if (md5 == null || !md5.equals(SeckillUtils.getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite"); // 秒杀数据被重写了
        }
        // 执行秒杀逻辑:减库存+增加购买明细
        Date nowTime = new Date();
        try {
            // 更新了库存,秒杀成功,增加明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userId);
            // 看是否该明细被重复插入,即用户是否重复秒杀
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存 ,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新库存记录,说明秒杀结束,rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息,commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userId);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    /**
     * 执行秒杀操作,存储过程
     *
     * @param seckillId
     * @param userId
     * @param md5
     */
    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userId, String md5) {
        if (md5 == null || !md5.equals(SeckillUtils.getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);

        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("id", userId);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行存储过程,result被赋值
        try {
            seckillDao.killByProcedure(map);
            // 获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userId);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}













