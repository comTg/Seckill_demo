package top.vetoer.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.vetoer.entity.SuccessKilled;

import java.util.List;

@Repository
public interface SuccessKilledDao {
    /**
     * 插入购买明细,可过滤重复
     * @param seckillId
     * @param userId
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userId") long userId);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @param userId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userId") long userId);

}
