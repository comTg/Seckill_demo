package top.vetoer.interceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class TokenValidator implements HandlerInterceptor {
    // 防止页面重复提交
    private final String TOKEN = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非get请求都有可能是提交表单
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            String currentToken = request.getParameter("judgeDuplicate");
            String token = String.valueOf(request.getSession().getAttribute(TOKEN));
            if (StringUtils.isEmpty(currentToken) || "null".equals(token) || !currentToken.equals(token)) {
                // 刷新提交,重定向到登录页面
                response.sendRedirect("/seckill/login");
            }
            // 正常提交表单,删除token
            request.getSession().removeAttribute(TOKEN);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        // GET 请求访问表单页面
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        // 生成 token 存储到 session 里，并且保存到 form 的 input 域
        String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        modelAndView.addObject(TOKEN, token);
        request.getSession().setAttribute(TOKEN, token);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
