package top.vetoer.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.vetoer.BaseTest;
import top.vetoer.entity.Seckill;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
    }

    @Test
    public void queryById() throws Exception {
          Seckill seckill = seckillDao.queryById(1004L);
          System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
    }

    @Test
    public void killByProcedure() throws Exception {
    }

}