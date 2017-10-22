package top.vetoer.interceptor;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MarkUrlInterceptor implements HandlerInterceptor {
    // 登录前页面拦截器,获取登录前一个页面,保存url
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object origin =   request.getRequestURL();
        if(origin!=null){
            String url = origin.toString();
            HttpSession session = request.getSession();
            session.setAttribute("url",url);
            System.out.println("MarkUrlInterceptor:"+url);
            return true;
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
