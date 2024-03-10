package com.tuituidan.openhub.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

    private static final String HTTPS = "https";

    private static final String HTTP = "http";

    /**
     * isHttps
     *
     * @param url url
     * @return boolean
     */
    public boolean isHttps(String url) {
        try {
            return HTTPS.equals(new URL(url).getProtocol());
        } catch (Exception ex) {
            throw new IllegalArgumentException("url解析错误", ex);
        }
    }

    /**
     * isHttps
     *
     * @param request request
     * @return boolean
     */
    public boolean isHttps(HttpServletRequest request) {
        return HTTPS.equalsIgnoreCase(request.getScheme());
    }

    /**
     * 获取url的根路径，如http://www.test.com/aa/xx.html -> http://www.test.com
     *
     * @param orgUrl 原始url
     * @return url
     */
    public static String getDomainUrl(String orgUrl) {
        try {
            URL url = new URL(orgUrl);
            StringBuilder sb = new StringBuilder(url.getProtocol())
                    .append("://").append(url.getHost());
            if (url.getPort() != -1) {
                sb.append(":").append(url.getPort());
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new IllegalArgumentException("url解析错误", ex);
        }
    }

    /**
     * 获取网站favicon
     *
     * @param domainUrl domainUrl
     * @return String
     */
    public static String getFromDocument(String domainUrl) {
        Document doc = HttpUtils.getJsoupDoc(domainUrl);
        if (Objects.isNull(doc)) {
            return StringUtils.EMPTY;
        }
        Elements links = doc.head().children().select("link[rel~=icon]");
        if (links.isEmpty()) {
            return "";
        }
        String href = links.get(0).attr("href");
        return requestFavicon(formatLinkIcon(domainUrl, href));
    }

    /**
     * 从link拿到的格式很多种，这里统一格式化一下
     *
     * @param domainUrl domainUrl
     * @param href href
     * @return String
     */
    private static String formatLinkIcon(String domainUrl, String href) {
        if (StringUtils.startsWith(href, HTTP)) {
            return href;
        }
        if (StringUtils.startsWith(href, "//")) {
            return StringUtils.substringBefore(domainUrl, "//") + href;
        }
        if (StringUtils.startsWith(href, "/")) {
            return domainUrl + href;
        }
        return domainUrl + "/" + href;
    }

    /**
     * 获取url的favicon
     *
     * @param url url
     * @return String
     */
    public static String requestFavicon(String url) {
        byte[] body = HttpUtils.toByteArray(url);
        // 要能实际获取到favicon的数据，如果返回是一个html文件，往往是鉴权导致重定向了
        if (body != null && !FileExtUtils.isHtml(body)) {
            if (HttpUtils.isHttps(RequestUtils.getRequest())) {
                return url;
            }
            if (!HttpUtils.isHttps(url)) {
                return url;
            }
            return IconUtils.saveIcon(url);
        }
        return "";
    }

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
