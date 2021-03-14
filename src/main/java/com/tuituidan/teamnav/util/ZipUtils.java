package com.tuituidan.teamnav.util;

import com.tuituidan.teamnav.bean.dto.CardZipDto;
import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.exception.UnzipException;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * ZipUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/8
 */
@UtilityClass
public class ZipUtils {

    private static final Charset CHARSET_GBK = Charset.forName("GBK");

    public static String unzip(CardZipDto zip) {
        try {
            String relDicName = FilenameUtils.getBaseName(zip.getName());
            File saveDicFile = new File(Consts.ROOT_DIR
                    + StringUtils.substringBeforeLast(zip.getPath(), "."));
            String url = StringExtUtils.format("/team-nav-modules/{}/index.html",
                    FilenameUtils.getBaseName(zip.getPath()));
            if (saveDicFile.exists()) {
                return url;
            }
            ZipFile zipFile = new ZipFile(Consts.ROOT_DIR + zip.getPath());
            zipFile.setCharset(CHARSET_GBK);
            String indexNmae = relDicName + "/index.html";
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            Assert.isTrue(fileHeaders.stream().anyMatch(item ->
                            StringUtils.equals(item.getFileName(), indexNmae)),
                    StringExtUtils.format("原型压缩包中应该存在【{}】这样的文件", indexNmae));
            zipFile.extractAll(Consts.ROOT_DIR + "/team-nav-modules");
            boolean rename = new File(StringExtUtils.format("{}/team-nav-modules/{}", Consts.ROOT_DIR, relDicName))
                    .renameTo(saveDicFile);
            return rename ? url : "";
        } catch (Exception ex) {
            throw new UnzipException("原型解压失败", ex);
        }
    }

}
