package top.vetoer.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.vetoer.BaseTest;
import top.vetoer.entity.User;

import static org.junit.Assert.*;

public class UserDaoTest extends BaseTest {
    @Autowired
    UserDao userDao;

    @Test
    public void addUser() throws Exception {
         int i = userDao.addUser(18928390l,"tg","tg0000");
         System.out.println(i);
    }

    @Test
    public void queryByPhone() throws Exception {
        long phone =  18928390l;
        User user = userDao.queryByPhone(phone);
        System.out.println(user);
    }

    @Test
    public void matchUser() throws Exception {
        long phone =  15151515151l;
        User user = userDao.matchUser(phone,"0000");
        System.out.println(user);
    }

}