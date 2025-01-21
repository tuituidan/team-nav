package com.tuituidan.openhub.service.thirdpart;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class WechatOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {
    // 根据微信文档，在请求地址中拼接上access_token和openid两个参数
    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .queryParam(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue())
                .queryParam("openid", "OPENID")
                .build()
                .toUri();
        return new RequestEntity<>(HttpMethod.GET, uri);
    }
}
