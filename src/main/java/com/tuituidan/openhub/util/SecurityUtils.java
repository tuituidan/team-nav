package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.vo.UserInfoVo;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * SecurityUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/30
 */
@Component
public class SecurityUtils implements ApplicationContextAware {

    private static boolean loginEnable = true;

    private static final UserInfoVo USER_INFO_VO = new UserInfoVo().setNick("管理员")
            .setAvatar("/assets/images/header.jpg");

    /**
     * 是否开启了登录功能
     *
     * @return boolean
     */
    public static boolean isLoginEnable() {
        return loginEnable;
    }

    /**
     * 获取当前登录用户
     *
     * @return UserInfoVo
     */
    public static UserInfoVo getUserInfo() {
        if (!loginEnable) {
            // 没有开启登录直接使用
            return USER_INFO_VO;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // 暂时只有管理员登录
            return USER_INFO_VO;
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        init(appContext.getEnvironment());
    }

    private static void init(Environment environment) {
        loginEnable = BooleanUtils.toBoolean(environment.getProperty("login.enable"));
    }

}
