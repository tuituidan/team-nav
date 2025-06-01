package com.tuituidan.openhub.util;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.UnzipException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
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
@Slf4j
public class ZipUtils {

    private static final Charset CHARSET_GBK = Charset.forName("GBK");

    /**
     * 解压静态网站，找到压缩包中的index.html，解压与index.html同级的所有文件和文件夹到根目录
     *
     * @param zipPath zipPath
     * @param id id
     */
    public static void unzip(String id, String zipPath) {
        try (ZipFile zipFile = new ZipFile(Consts.ROOT_DIR + zipPath)) {
            zipFile.setCharset(CHARSET_GBK);
            Assert.isTrue(zipFile.isValidZipFile(), "请上传ZIP文件");
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            List<String> indexPaths = fileHeaders.stream().map(FileHeader::getFileName)
                    .filter(fileName -> StringUtils.endsWith(fileName, "index.html"))
                    .collect(Collectors.toList());
            Assert.notEmpty(indexPaths, "静态网站压缩包中缺少index.html");
            String[] indexPathArr = indexPaths.get(0).split("/");
            String savePath = StringExtUtils.format("{}/ext-resources/modules/{}/", Consts.ROOT_DIR, id);
            zipFile.extractAll(savePath);
            if (indexPathArr.length == 1) {
                // index.html就在压缩包根目录，无需其他操作
                return;
            }
            // 临时解压目录
            String tempPath = savePath + StringUtils.join(indexPathArr, "/", 0, indexPathArr.length - 1);
            FileUtils.copyDirectory(new File(tempPath), new File(savePath));
            FileUtils.forceDelete(new File(savePath + indexPathArr[0]));
        } catch (ZipException ex) {
            throw new UnzipException("静态网站解压失败", ex);
        } catch (IOException ex) {
            throw new UnzipException("静态网站复制失败", ex);
        }
    }

    /**
     * unzip
     *
     * @param zipPath zipPath
     */
    public static void unzip(String zipPath) {
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            zipFile.setCharset(CHARSET_GBK);
            Assert.isTrue(zipFile.isValidZipFile(), "请上传ZIP文件");
            zipFile.extractAll(StringUtils.removeEndIgnoreCase(zipPath, ".zip"));
        } catch (IOException ex) {
            throw new UnzipException("文件解压失败", ex);
        }
    }

    /**
     * zip
     *
     * @param zipPath zipPath
     * @param paths paths
     */
    public static void zip(String zipPath, List<String> paths) {
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            for (String path : paths) {
                File file = new File(path);
                if (file.isDirectory()) {
                    zipFile.addFolder(file);
                } else {
                    zipFile.addFile(file);
                }
            }
        } catch (Exception ex) {
            throw new UnzipException("文件打包异常", ex);
        }
    }

}
