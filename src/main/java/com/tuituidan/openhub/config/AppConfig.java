package com.tuituidan.openhub.config;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.servlet.MultipartConfigElement;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/5
 */
@Configuration
public class AppConfig {

    /**
     * 连接超时时间.
     */
    private static final int CONNECT_TIMEOUT = 10000;

    /**
     * 读取数据时间.
     */
    private static final int READ_TIMEOUT = 60000;

    /**
     * 初始RestTemplate
     *
     * @return RestTemplate
     * @throws KeyStoreException KeyStoreException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws KeyManagementException KeyManagementException
     */
    @Bean
    public RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
                        .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true)
                        .build())).build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(READ_TIMEOUT);
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        return new RestTemplate(requestFactory);
    }

    /**
     * 明确指定上传文件的临时目录，避免Tomcat使用默认的tmp而被操作系统清理掉
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/temp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            Assert.isTrue(tmpFile.mkdirs(), "临时上传路径创建失败");
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }

}
