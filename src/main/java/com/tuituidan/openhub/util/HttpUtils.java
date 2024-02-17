package com.tuituidan.openhub.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * HttpUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/17
 */
@UtilityClass
public class HttpUtils {

    private static final int HTTP_TIMEOUT = 5000;

    /**
     * getJsoupDoc
     *
     * @param url url
     * @return Document
     */
    public Document getJsoupDoc(String url) {
        try {
            return Jsoup.connect(url)
                    .timeout(HTTP_TIMEOUT).get();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * toByteArray
     *
     * @param url url
     * @return byte[]
     */
    public byte[] toByteArray(String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            conn.setConnectTimeout(HTTP_TIMEOUT);
            conn.setReadTimeout(HTTP_TIMEOUT);
            return IOUtils.toByteArray(conn);
        } catch (Exception ex) {
            return null;
        } finally {
            IOUtils.close(conn);
        }
    }

}
