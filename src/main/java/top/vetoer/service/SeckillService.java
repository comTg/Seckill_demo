package top.vetoer.service;

import top.vetoer.dto.Exposer;
import top.vetoer.dto.SeckillExecution;
import top.vetoer.entity.Seckill;
import top.vetoer.exception.RepeatKillException;
import top.vetoer.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    /**
     * 查询全部的秒杀记录
     */
    List<Seckill> getSeckillList();
    /**
     * 查询单个秒杀记录
     */
    Seckill getById(long seckillId);

    /**
     * 根据输入名查询列表
     * @param name
     * @return
     */
    List<Seckill> queryByName(String name);
    /**
     * 在秒杀开启时输出秒杀接口的地址,否则输出系统时间和秒杀时间
     */
    Exposer exportSeckillUrl(long seckillId);
    /**
     * 执行秒杀操作,有可能失败,有可能成功,所以我们要抛出异常
     */
    SeckillExecution executeSeckill(long seckillId,long userId,String md5)
            throws SeckillException,RepeatKillException,SeckillException;
    /**
     * 执行秒杀操作,存储过程
     */
    SeckillExecution executeSeckillProcedure(long seckillId,long userId,String md5)
            throws SeckillException,RepeatKillException,SeckillException;





















}
