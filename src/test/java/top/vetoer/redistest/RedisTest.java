package top.vetoer.redistest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.vetoer.BaseTest;
import top.vetoer.dao.cache.RedisDao;

public class RedisTest extends BaseTest {
    @Autowired
    public RedisDao redisDao;
    @Test
    public void testConnection(){
          Jedis jedis = redisDao.getJedis();
          System.out.println(jedis);
          System.out.println("--------------");
          jedis.close();
    }
}
