package top.vetoer.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.vetoer.entity.SuccessKilled;
import top.vetoer.entity.User;

import java.util.List;

@Repository
public interface UserDao {
    /**
     * 注册用户
     * @param phone
     * @param password
     * @return
     */
    int addUser(@Param("phone")long phone,@Param("name")String name,@Param("password")String password);

    /**
     * 根据手机号码查找数据库中是否有该用户
     */
    User queryByPhone(long phone);

    /**
     * 用户登录是查询手机号码和密码是否正确
     * @param phone
     * @param password
     * @return
     */
    User matchUser(@Param("phone")long phone,@Param("password")String password);

    /**
     * 根据用户Id查询该用户秒杀下的所有订单
     * @param userId
     * @return
     */
    List<SuccessKilled> queryById(Long userId);
}
