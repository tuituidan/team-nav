package com.tuituidan.openhub.util;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
     * saveIcon
     *
     * @param iconUrl iconUrl
     * @return String
     */
    public static String saveIcon(String iconUrl) {
        String path = StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), FilenameUtils.getExtension(iconUrl));
        File saveFile = new File(Consts.ROOT_DIR + path);
        try {
            FileUtils.forceMkdirParent(saveFile);
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
        try (OutputStream outputStream = new FileOutputStream(saveFile)) {
            IOUtils.copy(new URL(iconUrl), outputStream);
            return path;
        } catch (Exception ex) {
            throw new ResourceWriteException("网络图标保存失败", ex);
        }
    }

}
