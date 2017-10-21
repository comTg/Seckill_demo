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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/seckill")
public class UserController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(Model model){
        return "register";
    }
    
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        if(userService.register(user)){
            // 注册成功,跳转到登录页面
            return "login";
        }
        model.addAttribute("error","登录失败");
        return "register";
    }



    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(Model model,HttpServletRequest req){
        String url = req.getRequestURL().toString();
        model.addAttribute("url",url);
        System.out.println(url+"\n------------");
        return "login";
    }

    // 登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(Model model, HttpServletRequest req, HttpSession session){
        // 从request中获取表单数据
        long phone = Long.parseLong(req.getParameter("phone"));
        String password = req.getParameter("password");
        User user = userService.login(phone,password);
        if(user!=null){
            // 登录成功
            session.setAttribute("userId",user.getUserId());
            String url = req.getParameter("url");
            return "redirect:/seckill/list";
        }
        model.addAttribute("error","登录失败");
        return "login";
    }

    // 登出,从session中清除数据
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(Model model,HttpSession session){
        session.invalidate();
        return "redirect:/seckill/list";
    }












}
