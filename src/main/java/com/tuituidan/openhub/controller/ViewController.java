package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.service.SettingService;
import com.tuituidan.openhub.util.RequestUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Resource
    private SettingService settingService;

    @Value("${change-password.enable}")
    private Boolean changePwdEnable;

    @Value("${project.version}")
    private String projectVersion;

    /**
     * 首页访问
     *
     * @param request request
     * @return String
     */
    @GetMapping({"/", "index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("navName", settingService.getNavName());
        request.setAttribute("logoPath", settingService.getLogoPath());
        request.setAttribute("isIe", RequestUtils.isIe());
        request.setAttribute("countdown", settingService.getCountdown());
        request.setAttribute("cutOverSpeed", settingService.getCutOverSpeed());
        return "index";
    }

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

    /**
     * 后端管理页面
     *
     * @param request request
     * @return String
     */
    @GetMapping("/admin/**")
    public String admin(HttpServletRequest request) {
        request.setAttribute("page", StringUtils.substringAfterLast(request.getServletPath(), "/"));
        request.setAttribute("navName", settingService.getNavName());
        request.setAttribute("logoPath", settingService.getLogoPath());
        request.setAttribute("userInfo", SecurityUtils.getUserInfo());
        request.setAttribute("loginEnable", SecurityUtils.isLoginEnable());
        request.setAttribute("changePwdEnable", changePwdEnable);
        request.setAttribute("projectVersion", projectVersion);
        request.setAttribute("countdown", settingService.getCountdown());
        return "admin";
    }

}
