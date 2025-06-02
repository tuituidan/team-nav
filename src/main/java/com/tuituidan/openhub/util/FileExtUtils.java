package com.tuituidan.openhub.util;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.util.thread.CompletableUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileExtUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/15
 */
@Slf4j
@UtilityClass
public class FileExtUtils {

    private static final String FILE_HEADER_HTML = "3C21444F";

    private static final String FILE_HEADER_ZIP = "504B0304";

    /**
     * isHtml.
     *
     * @param data data
     * @return boolean
     */
    public static boolean isHtml(byte[] data) {
        return fileType(FILE_HEADER_HTML, data);
    }

    /**
     * isZip.
     *
     * @param data data
     * @return boolean
     */
    public static boolean isZip(byte[] data) {
        return fileType(FILE_HEADER_ZIP, data);
    }

    private static boolean fileType(String fileHeader, byte[] data) {
        byte[] src = new byte[4];
        System.arraycopy(data, 0, src, 0, src.length);
        StringBuilder builder = new StringBuilder();
        for (byte bt : src) {
            String hv = Integer.toHexString(bt & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return fileHeader.equals(builder.toString());
    }

    /**
     * deleteFiles
     *
     * @param sync sync
     * @param paths paths
     */
    public static void deleteFiles(boolean sync, String... paths) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (String path : paths) {
            File file = new File(Consts.ROOT_DIR + path);
            if (!file.exists()) {
                continue;
            }
            futures.add(CompletableUtils.runAsync(() -> {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException ex) {
                    log.error("删除原文件失败：【{}】", path, ex);
                }
            }));
        }
        if (sync && CollectionUtils.isNotEmpty(futures)) {
            CompletableUtils.waitAll(futures);
        }
    }

    /**
     * deleteOnExists
     *
     * @param path path
     */
    public static void deleteOnExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException ex) {
                throw new IllegalArgumentException("文件删除失败", ex);
            }
        }
    }

    /**
     * transferTo
     *
     * @param file file
     * @param zipPath zipPath
     */
    public static void transferTo(MultipartFile file, String zipPath) {
        deleteOnExists(zipPath);
        try {
            file.transferTo(new File(zipPath));
        } catch (IOException ex) {
            throw new IllegalArgumentException("文件转储失败", ex);
        }
    }

    /**
     * readString
     *
     * @param path path
     * @return String
     */
    public static String readString(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new IllegalArgumentException("文件读取失败", ex);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * writeString
     *
     * @param path path
     * @param source source
     */
    public static void writeString(String path, String source) {
        try {
            FileUtils.writeStringToFile(new File(path), source, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalArgumentException("文件存储失败", ex);
        }
    }

    /**
     * forceMkdirParent
     *
     * @param path path
     */
    public static File forceMkdirParent(String path) {
        File saveFile = new File(path);
        try {
            FileUtils.forceMkdirParent(saveFile);
            return saveFile;
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
    }

}
