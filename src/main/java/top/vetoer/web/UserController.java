package top.vetoer.web;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import top.vetoer.entity.User;
import top.vetoer.service.UserService;
import top.vetoer.utils.SeckillUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/seckill")
public class UserController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        if (userService.register(user)) {
            // 注册成功,跳转到登录页面
            return "login";
        }
        model.addAttribute("error", "登录失败");
        return "register";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest req) {
        Object origin = req.getSession().getAttribute("url");
        String url = "";
        if (origin != null) {
            url = origin.toString();
            System.out.println("UserController:" + url);
            if (url.contains("login")) {
                url = "/seckill/list";
            }
        } else {
            url = "/seckill/list";
        }
        model.addAttribute("url", url);
        return "login";
    }

    // 登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, HttpServletRequest req, HttpServletResponse resp) {
        // 从request中获取表单数据
        long phone = Long.parseLong(req.getParameter("phone"));
        String password = req.getParameter("password");
        User user = userService.login(phone, password);
        if (user != null) {
            // 登录成功,在Session中写入userId
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getName());
            /*
            写入cookie,使用md5混淆
             */
            Cookie cookie = new Cookie("USERID",
                    SeckillUtils.getMD5(user.getUserId()));
            // 设置关闭浏览器时删除cookie
            cookie.setMaxAge(-1);
            cookie.setPath("/seckill");
            resp.addCookie(cookie);
            String url = req.getParameter("url");
            if(url==null){
                url = "/seckill/list";
            }
            System.out.println("UserController-login:"+url);
            return "redirect:" + url;
        }
        model.addAttribute("error", "登录失败");
        return "login";
    }

    // 登出,从session中清除数据
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model,
                         HttpServletRequest req,
                         HttpServletResponse resp, HttpSession session) {
        // 获取cookie数组
        Cookie[] cookies = req.getCookies();
        // 迭代查找cookie并删除
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/seckill");
            resp.addCookie(cookie);
        }
        session.invalidate();
        return "redirect:/seckill/list";
    }


}
