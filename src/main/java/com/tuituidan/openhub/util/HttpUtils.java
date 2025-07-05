package com.tuituidan.openhub.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.tuituidan.openhub.bean.dto.CardDynamicBuilder;
import com.tuituidan.openhub.bean.dto.KeyValueDto;
import com.tuituidan.openhub.consts.AuthTypeEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
     * isHttp
     *
     * @param url url
     * @return boolean
     */
    public boolean isHttp(String url) {
        return StringUtils.startsWith(url, HTTP);
    }

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
        if (ArrayUtils.isNotEmpty(body) && !FileExtUtils.isHtml(body)) {
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
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        } finally {
            IOUtils.close(conn);
        }
    }

    /**
     * buildQueryParams
     *
     * @param url url
     * @param queryParams queryParams
     * @return String
     */
    public static String buildQueryParams(String url, List<KeyValueDto> queryParams) {
        if (CollectionUtils.isEmpty(queryParams)) {
            return url;
        }
        List<KeyValueDto> params = queryParams.stream().filter(it -> StringUtils.isNoneBlank(it.getKey(),
                it.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(params)) {
            return url;
        }
        UriComponents build = UriComponentsBuilder.fromHttpUrl(url).build();
        if (StringUtils.isBlank(build.getQuery())) {
            return url + "?" + queryParams.stream().map(it -> it.getKey() + "=" + it.getValue())
                    .collect(Collectors.joining("&"));
        }
        StringBuilder sb = new StringBuilder(url);
        for (KeyValueDto item : queryParams) {
            if (!build.getQueryParams().containsKey(item.getKey())) {
                sb.append("&").append(item.getKey()).append("=").append(item.getValue());
            }
        }
        return sb.toString();
    }

    /**
     * buildHttpEntity
     *
     * @param dynamicBuilder dynamicBuilder
     * @return HttpEntity
     */
    public static HttpEntity<Object> buildHttpEntity(CardDynamicBuilder dynamicBuilder) {
        HttpHeaders httpHeaders = buildHttpHeaders(dynamicBuilder);
        if (JsonFactory.FORMAT_NAME_JSON.equalsIgnoreCase(dynamicBuilder.getBodyType())) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity<>(dynamicBuilder.getBodyJsonData(), httpHeaders);
        }
        if (FileUploadBase.FORM_DATA.equalsIgnoreCase(dynamicBuilder.getBodyType())) {
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
            return new HttpEntity<>(paramMap, httpHeaders);
        }
        return new HttpEntity<>(httpHeaders);
    }

    private static HttpHeaders buildHttpHeaders(CardDynamicBuilder dynamicBuilder) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (AuthTypeEnum.KEY_VALUE.getType().equals(dynamicBuilder.getAuthType())) {
            for (KeyValueDto item : dynamicBuilder.getAuthKeyValues()) {
                if (StringUtils.isNoneBlank(item.getKey(), item.getValue())) {
                    httpHeaders.add(item.getKey(), item.getValue());
                }
            }
            return httpHeaders;
        }
        if (AuthTypeEnum.BASIC.getType().equals(dynamicBuilder.getAuthType())) {
            httpHeaders.setBasicAuth(dynamicBuilder.getBasicUsername(), dynamicBuilder.getBasicPassword(),
                    StandardCharsets.UTF_8);
            return httpHeaders;
        }
        if (AuthTypeEnum.BEARER.getType().equals(dynamicBuilder.getAuthType())) {
            httpHeaders.setBearerAuth(dynamicBuilder.getBearerToken());
            return httpHeaders;
        }
        if (AuthTypeEnum.JWT.getType().equals(dynamicBuilder.getAuthType())) {
            List<KeyValueDto> list = dynamicBuilder.getJwtPayload().stream()
                    .filter(it -> StringUtils.isNoneBlank(it.getKey(), it.getValue())).collect(Collectors.toList());
            Map<String, Object> map = list.stream().collect(Collectors.toMap(KeyValueDto::getKey,
                    KeyValueDto::getValue));
            SignatureAlgorithm algorithm = SignatureAlgorithm.forName(dynamicBuilder.getJwtAlgorithm());
            httpHeaders.setBearerAuth(Jwts.builder().setClaims(map).signWith(algorithm,
                    dynamicBuilder.getJwtSecret()).compact());
            return httpHeaders;
        }
        return httpHeaders;
    }

}
