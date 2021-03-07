package com.tuituidan.teamnav.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
     * 初始RestTemplate.
     *
     * @return RestTemplate
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
}
