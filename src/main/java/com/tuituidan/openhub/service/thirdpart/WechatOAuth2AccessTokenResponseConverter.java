package com.tuituidan.openhub.service.thirdpart;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;

public class WechatOAuth2AccessTokenResponseConverter implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {
    private static final DefaultMapOAuth2AccessTokenResponseConverter delegate = new DefaultMapOAuth2AccessTokenResponseConverter();
    //响应中缺少token_type字段，为避免报错默认填充，剩余部分依然委托给默认的DefaultMapOAuth2AccessTokenResponseConverter处理
    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> source) {
        source.put(OAuth2ParameterNames.TOKEN_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());
        return delegate.convert(source);
    }
}
