package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.util.RequestUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * IndexController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/21
 */
@Controller
public class ViewController {

    @Value("${nav-name}")
    private String navName;

    /**
     * 首页访问
     *
     * @param request request
     * @return String
     */
    @GetMapping({"/", "index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("navName", navName);
        request.setAttribute("isIe", RequestUtils.isIe());
        return "index";
    }

    /**
     * login
     *
     * @return String
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 后端管理页面
     *
     * @param request request
     * @return String
     */
    @GetMapping("/admin/**")
    public String admin(HttpServletRequest request) {
        request.setAttribute("page", StringUtils.substringAfterLast(request.getServletPath(), "/"));
        request.setAttribute("navName", navName);
        request.setAttribute("userInfo", SecurityUtils.getUserInfo());
        request.setAttribute("loginEnable", SecurityUtils.isLoginEnable());
        return "admin";
    }

}
