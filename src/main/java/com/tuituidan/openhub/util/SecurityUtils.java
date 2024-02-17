package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.entity.User;
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
     * 获取当前登录用户
     *
     * @return UserInfoVo
     */
    public static User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return BeanExtUtils.convert(authentication.getPrincipal(), User::new);
    }

}
