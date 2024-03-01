package com.tuituidan.openhub.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
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
