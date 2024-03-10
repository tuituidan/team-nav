package com.tuituidan.openhub.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 汉语转拼音
     *
     * @param source source
     * @return String
     */
    public static String toHanYuPinyin(String source) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 用v表示ü
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        // 不要音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 大小写设置，默认小二 format.setCaseType(HanyuPinyinCaseType.UPPERCASE)
        try {
            return PinyinHelper.toHanYuPinyinString(source, format, "", true);
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            log.error("汉语转拼音失败", ex);
            return StringUtils.EMPTY;
        }
    }

}
