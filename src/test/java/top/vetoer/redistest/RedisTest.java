package top.vetoer.redistest;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTest {
    @Test
    public void testConnection(){
        JedisPool pool = new JedisPool("67.216.218.113",6379);
        Jedis jedis = pool.getResource();
        jedis.auth("tgredis");
        System.out.println(jedis);
        System.out.println("------------");
        jedis.set("name","tg");
        System.out.println(jedis.get("name"));

        jedis.close();
        pool.close();
    }
}
