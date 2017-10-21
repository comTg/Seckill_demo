package top.vetoer.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.vetoer.BaseTest;
import top.vetoer.dto.Exposer;
import top.vetoer.dto.SeckillExecution;
import top.vetoer.entity.Seckill;
import top.vetoer.service.SeckillService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SeckillServiceImplTest extends BaseTest {

    @Autowired
    SeckillService service;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> lists = service.getSeckillList();
        System.out.println(Arrays.asList(lists));
    }

    @Test
    public void getById() throws Exception {
         long seckillId = 1004L;
         Seckill seckill = service.getById(seckillId);
         System.out.println(seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long seckillId = 1004L;
        Exposer exposer = service.exportSeckillUrl(seckillId);
        System.out.println(exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        long seckillId = 1004L;
        long userId = 100l;
        SeckillExecution execution = service.executeSeckill(seckillId,userId,"e19fa2c284deb81c7071b3ed74c7889c");
        System.out.println(execution);
    }

    @Test
    public void executeSeckillProcedure() throws Exception {
        SeckillExecution execution = service.executeSeckillProcedure(1005l,100l,"e19fa2c284deb81c7071b3ed74c7889c");
        System.out.println(execution);
    }

}