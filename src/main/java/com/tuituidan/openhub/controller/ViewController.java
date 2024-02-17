package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.util.RequestUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.WebAttributes;
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

    /**
     * login
     *
     * @return String
     */
    @GetMapping("/login")
    public String login() {
        HttpServletRequest request = RequestUtils.getRequest();
        Object error = request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (error instanceof Exception) {
            request.setAttribute("errorMsg", ((Exception) error).getMessage());
            // 只使用一次就移除，再刷新页面就不显示了
            request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        return "login";
    }
}
