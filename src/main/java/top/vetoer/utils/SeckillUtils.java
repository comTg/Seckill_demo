package top.vetoer.utils;

import org.springframework.util.DigestUtils;

public class SeckillUtils {
    // 加入一个混淆字符串(秒杀接口)的salt,为了避免用户猜出我们的md5值,值任意给,越复杂越好
    private static final String SALT = "com.sdfasd+)sdf2----";

    public static String getMD5(Object receive){
        String base = receive + "/" + SALT;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
