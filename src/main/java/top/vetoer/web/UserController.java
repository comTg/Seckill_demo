package top.vetoer.web;

import com.google.code.kaptcha.Producer;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import top.vetoer.entity.User;
import top.vetoer.service.UserService;
import top.vetoer.utils.SeckillUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

@Controller
@RequestMapping("/seckill")
public class UserController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 存入session标识
    private final String VALIDATE_CODE = "VALIDATE_CODE";
    // 验证码有效期标识
    private final String EXPIRE_TIME = "EXPIRE_TIME";


    @Autowired
    private UserService userService;
    @Autowired
    private Producer captchaProducer;

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
    public String login(Model model, HttpSession session) {
        String url = String.valueOf(session.getAttribute("url"));
        if (url != "null") {
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
        HttpSession session = req.getSession();
//        String submitDuplicate = String.valueOf(req.getAttribute("submitDuplicate"));
//        if(!"null".equals(submitDuplicate) && "true".equals(submitDuplicate)){
//            // 重复提交,重定向到login
//            req.removeAttribute("submitDuplicate");
//            return "redirect:/seckill/login";
//        }
        // 1.校验验证码是否过期
        String currentExpireTime = String.valueOf(session.getAttribute(EXPIRE_TIME));
        if(!"null".equals(currentExpireTime) &&
                System.currentTimeMillis()>Long.parseLong(currentExpireTime)){
            //  验证码过期
            model.addAttribute("error","验证码过期,请重试");
            return "login";
        }
        // 2.判断验证码是否为空以及输入验证码是否正确
        String currentValidateCode = req.getParameter("validateCode");
        String validateCode = String.valueOf(session.getAttribute(VALIDATE_CODE));
        if(StringUtils.isEmpty(currentValidateCode)||
                StringUtils.isEmpty(validateCode) ||
                !SeckillUtils.encodeBase64(currentValidateCode).equals(validateCode)){
            model.addAttribute("error","验证码输入错误,请重试");
            return "login";
        }
        // 从request中获取表单数据
        long phone = Long.parseLong(req.getParameter("phone"));
        String password = req.getParameter("password");
        User user = userService.login(phone, password);
        if (user != null) {
            // 登录成功,在Session中写入userId
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
        model.addAttribute("error", "用户名或密码错误");
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

    @RequestMapping(value = "/loadValidateCode",method = RequestMethod.GET)
    public void loadValidateCode(HttpServletRequest req, HttpServletResponse resp){
        try {
            HttpSession httpSession = req.getSession();
            //设置清除浏览器缓存
            resp.setDateHeader("Expires", 0);
            resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
            resp.setHeader("Pragma", "no-cache");
            resp.setContentType("image/png");
            // 设置验证码有效期限:1分钟
            long expireTime = System.currentTimeMillis()+60000;
            // 生成验证码,将验证码放入session中
            String validateCode = captchaProducer.createText();
            httpSession.setAttribute(VALIDATE_CODE, SeckillUtils.encodeBase64(validateCode));
            httpSession.setAttribute(EXPIRE_TIME,expireTime);

            // 输出验证码图片
            BufferedImage bufferedImage = captchaProducer.createImage(validateCode);
            ServletOutputStream out = resp.getOutputStream();
            ImageIO.write(bufferedImage,"png",out);
            out.flush();
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
