package top.vetoer.service;

import top.vetoer.entity.SuccessKilled;
import top.vetoer.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 查找数据库中是否有当前用户
     * @param phone
     * @return
     */
    User findByPhone(long phone);

    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    User login(long phone,String password);

    /**
     * 根据用户id查询当前用户所秒杀的订单
     * @param userId
     * @return
     */
    List<SuccessKilled> getOrdersById(long userId);
}
