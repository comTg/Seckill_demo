package top.vetoer.web;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import static org.junit.Assert.*;

public class UserControllerTest {
    // 测试md5值
    @Test
    public void testMd5(){
        String salt = "sdsdfsdf";
        String md5Id = "1004/"+salt;
        String md5 = DigestUtils.md5DigestAsHex(md5Id.getBytes());
    }
}