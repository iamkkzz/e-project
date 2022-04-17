package com.kkzz.mall.member.interceptor;

import com.kkzz.common.constant.AuthServerConstant;
import com.kkzz.common.vo.MemberRespVo;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean match = new AntPathMatcher().match("/member/**", request.getRequestURI());
        if (match) {
            return true;
        }
        HttpSession session = request.getSession();
        MemberRespVo attribute = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null) {
            //已经登录
            loginUser.set(attribute);
            return true;
        } else {
            //未登录,则跳转到登录页面
            session.setAttribute("msg", "请先登录");
            response.sendRedirect("http://auth.mall.com/login.html");
            return false;
        }
    }
}
