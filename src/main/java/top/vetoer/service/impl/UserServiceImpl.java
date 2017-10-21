package top.vetoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vetoer.dao.UserDao;
import top.vetoer.entity.SuccessKilled;
import top.vetoer.entity.User;
import top.vetoer.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        // 判断用户是否为空
        if(validationUser(user)){
            int result = userDao.addUser(user.getPhone(),user.getName(),user.getPassword());
            if(result>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 查找数据库中是否有当前用户
     *
     * @param phone
     * @return
     */
    @Override
    public User findByPhone(long phone) {
        if(phone!=0){
            User user = userDao.queryByPhone(phone);
            return user;
        }
        return null;
    }

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public User login(long phone, String password) {
        if(phone!=0 && password!=null && password.length()>3){
            User user = userDao.matchUser(phone,password);
            return user;
        }
        return null;
    }

    /**
     * 根据用户id查询当前用户所秒杀的订单
     *
     * @param userId
     * @return
     */
    @Override
    public List<SuccessKilled> getOrdersById(long userId) {
        if(userId!=0){
            List<SuccessKilled> lists = getOrdersById(userId);
            return lists;
        }
        return null;
    }

    // 判断接收到的用户是否为空
    public boolean validationUser(User user){
        if(user!=null){
            if(user.getPhone()>0 && user.getPassword()!=null && user.getName()!=null){
                   return true;
            }
        }
        return false;
    }
}
