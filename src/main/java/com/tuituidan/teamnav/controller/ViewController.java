package com.tuituidan.teamnav.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @GetMapping({"/", "index.html"})
    public String index(ModelMap modelMap) {
        return "index";
    }

    @GetMapping("/admin/**")
    public String admin(HttpServletRequest request) {
        request.setAttribute("page", StringUtils.substringAfterLast(request.getServletPath(), "/"));
        return "admin";
    }
}
