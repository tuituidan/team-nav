package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * IconUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/3/10
 */
@UtilityClass
public class IconUtils {

    /**
     * 从base64字符串中获取文件扩展名.
     */
    private static final Pattern PATTERN = Pattern.compile("data:image/(.*?);base64");

    /**
     * buildTextIcon
     *
     * @param text text
     * @return CardIconDto
     */
    public static CardIconDto buildTextIcon(String text) {
        return new CardIconDto().setText(StringUtils.truncate(text, 2))
                .setColor(RandomColorUtils.generate());
    }

    /**
     * saveBase64.
     *
     * @param source String
     * @return String
     */
    public static String saveBase64(String source) {
        String[] datas = StringUtils.split(source, ",");
        Matcher matcher = PATTERN.matcher(datas[0]);
        Assert.isTrue(matcher.find(), "base64图片数据解析失败");
        String path = buildIconSavePath(matcher.group(1));
        File saveFile = FileExtUtils.forceMkdirParent(Consts.ROOT_DIR + path);
        try {
            FileUtils.writeByteArrayToFile(saveFile, Base64.getDecoder().decode(datas[1]));
            return path;
        } catch (IOException ex) {
            throw new ResourceWriteException("base64图标保存失败", ex);
        }
    }

    /**
     * saveIcon
     *
     * @param iconUrl iconUrl
     * @return String
     */
    public static String saveIcon(String iconUrl) {
        String path = buildIconSavePath(FilenameUtils.getExtension(iconUrl));
        File saveFile = FileExtUtils.forceMkdirParent(Consts.ROOT_DIR + path);
        try (OutputStream outputStream = new FileOutputStream(saveFile)) {
            IOUtils.copy(new URL(iconUrl), outputStream);
            return path;
        } catch (Exception ex) {
            throw new ResourceWriteException("网络图标保存失败", ex);
        }
    }

    private static String buildIconSavePath(String ext) {
        return StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), ext);
    }

}
