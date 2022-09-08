package com.tuituidan.openhub.util;

import lombok.experimental.UtilityClass;

/**
 * FileExtUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/15
 */
@UtilityClass
public class FileTypeUtils {

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
}
