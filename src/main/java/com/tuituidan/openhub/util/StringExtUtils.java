package com.tuituidan.openhub.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * StringKit.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Slf4j
@UtilityClass
public class StringExtUtils {

    /**
     * uuid.
     *
     * @return String
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 使用 Slf4j 中的字符串格式化方式来格式化字符串.
     *
     * @param pattern 待格式化的字符串
     * @param args 参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... args) {
        return pattern == null ? "" : MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

    /**
     * urlEncode.
     *
     * @param source source
     * @return String
     */
    public static String urlEncode(String source) {
        try {
            return URLEncoder.encode(source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    /**
     * urlDecode.
     *
     * @param source source
     * @return String
     */
    public static String urlDecode(String source) {
        try {
            return URLDecoder.decode(source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }
}
