package top.vetoer.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.vetoer.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return  如果影响行数=1,表示减库存成功
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀商品
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表,分页查询
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryPage(@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 查询列表中商品数量
     * @return
     */
    Integer getProductsCount();

    /**
     * 查询全部
     * @return
     */
    List<Seckill> queryAll();

    /**
     * 根据输入名查找匹配秒杀商品
     * @param name
     * @return
     */
    List<Seckill> queryByName(String name);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);

}
