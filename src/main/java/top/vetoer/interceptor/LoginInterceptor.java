package top.vetoer.interceptor;

import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        HttpSession session = request.getSession();
        if(url!=null){
            session.setAttribute("url",url);
            System.out.println("loginInterceptor:"+url);
        }
        if(session.getAttribute("userId")!=null){
            // session中含有id属性,用户已登录
            return true;
        }
        // 没有登录,跳转到登录页面
       //request.getRequestDispatcher("/seckill/login").forward(request,response);
        response.sendRedirect(request.getContextPath()+"/seckill/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
