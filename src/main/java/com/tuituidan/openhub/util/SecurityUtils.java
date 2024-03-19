package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.entity.User;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/30
 */
@UtilityClass
public class SecurityUtils {

    /**
     * isLogin
     *
     * @return boolean
     */
    public boolean isLogin() {
        return Objects.nonNull(getUserInfo());
    }

    /**
     * getId
     *
     * @return String
     */
    public String getId() {
        return Objects.requireNonNull(getUserInfo()).getId();
    }

    /**
     * isAdmin
     *
     * @param userInfo userInfo
     * @return boolean
     */
    public static boolean isAdmin(User userInfo) {
        return userInfo != null && ("1".equals(userInfo.getId())
                || (userInfo.getRoleIds() != null && userInfo.getRoleIds().contains("1")));
    }

    /**
     * 获取当前登录用户
     *
     * @return UserInfoVo
     */
    public static User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

}
