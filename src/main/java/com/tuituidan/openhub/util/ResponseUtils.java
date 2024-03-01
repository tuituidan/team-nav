package com.tuituidan.openhub.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ResponseUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@UtilityClass
public class ResponseUtils {

    /**
     * 文件下载.
     *
     * @param fileName fileName
     * @param path path
     */
    public static void download(String fileName, String path) {
        try (InputStream in = new FileInputStream(path);
                OutputStream outputStream = getHttpResponse(fileName).getOutputStream()) {
            IOUtils.copy(in, outputStream);
        } catch (IOException ex) {
            throw new ResourceAccessException("下载失败", ex);
        }
    }

    /**
     * 文件下载.
     *
     * @param fileName fileName
     * @param inputStream inputStream
     */
    public static void download(String fileName, InputStream inputStream) {
        try (InputStream in = inputStream;
                OutputStream outputStream = getHttpResponse(fileName).getOutputStream()) {
            IOUtils.copy(in, outputStream);
        } catch (IOException ex) {
            throw new ResourceAccessException("下载失败", ex);
        }
    }

    /**
     * 获取下载头信息.
     *
     * @param fileName fileName
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse(String fileName) {
        HttpServletResponse response = getHttpResponse();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = fileName.replaceAll("\\s*", "");
        fileName = StringExtUtils.urlEncode(fileName);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        return response;
    }

    /**
     * 得到reponse.
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletResponse"));
    }

}
