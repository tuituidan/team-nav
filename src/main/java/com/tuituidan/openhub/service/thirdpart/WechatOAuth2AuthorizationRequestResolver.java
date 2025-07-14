package com.tuituidan.openhub.service.thirdpart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@Component
@Slf4j
public class WechatOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {


    final private ClientRegistrationRepository clientRegistrationRepository;

    public WechatOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    private void parametersConsumer(Map<String, Object> parameters) {
        Object clientId = parameters.get(OAuth2ParameterNames.CLIENT_ID);
        Object redirectUri = parameters.get(OAuth2ParameterNames.REDIRECT_URI);
        Object responseType = parameters.get(OAuth2ParameterNames.RESPONSE_TYPE);
        Object scope = parameters.get(OAuth2ParameterNames.SCOPE);
        Object state = parameters.get(OAuth2ParameterNames.STATE);
        // 清除掉原来所有的参数
        parameters.clear();
        // 重新调整顺序
        parameters.put("appid", clientId);// 修改clientId参数名称为appid
        parameters.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        parameters.put(OAuth2ParameterNames.RESPONSE_TYPE, responseType);
        parameters.put(OAuth2ParameterNames.SCOPE, scope);
        parameters.put(OAuth2ParameterNames.STATE, state);

    }

    private URI authorizationRequestUriFunction(UriBuilder builder) {
        // 添加#wechat_redirect
        builder.fragment("wechat_redirect");// 添加#wechat_redirect
        return builder.build();
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String authorizationRequestBaseUri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
        // 参考框架内默认的实例构造方法
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, authorizationRequestBaseUri);
        // 设置OAuth2AuthorizationRequest.builder的定制逻
        resolver.setAuthorizationRequestCustomizer(builder -> builder.parameters(this::parametersConsumer).authorizationRequestUri(this::authorizationRequestUriFunction));
        return resolver.resolve(request);

    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

        return resolve(request);
    }
}
