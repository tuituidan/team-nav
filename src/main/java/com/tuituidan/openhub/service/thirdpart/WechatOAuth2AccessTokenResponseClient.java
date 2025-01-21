package com.tuituidan.openhub.service.thirdpart;


import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class WechatOAuth2AccessTokenResponseClient {

    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        // 注入自定义WechatOAuth2AuthorizationCodeGrantRequestEntityConverter
        client.setRequestEntityConverter(new WechatOAuth2AuthorizationCodeGrantRequestEntityConverter());
        // 创建一个OAuth2AccessTokenResponseHttpMessageConverter对象，设置支持的MediaType为text/plain
        OAuth2AccessTokenResponseHttpMessageConverter messageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        messageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN));
        messageConverter.setAccessTokenResponseConverter(new WechatOAuth2AccessTokenResponseConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), messageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setRestOperations(restTemplate);
        return client;
    }
}
