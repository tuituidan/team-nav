package com.tuituidan.teamnav.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * LoginInterceptor.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/9/23
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/edit.html".equals(request.getServletPath())) {
            response.sendRedirect("/noauth.html");
        }
        return true;
    }
}
