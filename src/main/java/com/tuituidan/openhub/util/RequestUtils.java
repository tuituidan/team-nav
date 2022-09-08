package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.vo.UserInfoVo;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * RequestUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/11/26
 */
@UtilityClass
public class RequestUtils {

    private static final UserInfoVo USER_INFO_VO = new UserInfoVo().setNick("管理员")
            .setAvatar("/assets/images/header.jpg");

    /**
     * getUserInfo
     *
     * @return UserInfoVo
     */
    public static UserInfoVo getUserInfo() {
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

    /**
     * 得到reponse.
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletResponse"));
    }

    /**
     * 得到request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletRequest"));
    }

    private static final String[] IE_AGENT = {"MSIE", "Trident", "Edge"};

    /**
     * isIe
     *
     * @return boolean
     */
    public static boolean isIe() {
        String userAgent = getRequest().getHeader("User-Agent");
        return StringUtils.indexOfAny(userAgent, IE_AGENT) > -1;
    }

}
