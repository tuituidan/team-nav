package com.tuituidan.openhub.service.thirdpart;

import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.bean.entity.ThirdPartUser;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.repository.RoleUserRepository;
import com.tuituidan.openhub.repository.ThirdPartUserRepository;
import com.tuituidan.openhub.repository.UserRepository;
import com.tuituidan.openhub.util.StringExtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WechatOAuth2UserLoginService extends DefaultOAuth2UserService {

    @Resource
    ThirdPartUserRepository thirdPartUserRepository;

    @Resource
    UserRepository userRepository;
    @Resource
    RoleUserRepository roleUserRepository;
    @Value("${spring.security.user.password}")
    private String defPassword;

    WechatOAuth2UserLoginService() {
        // 注入自定义的requestEntityConverter
        this.setRequestEntityConverter(new WechatOAuth2UserRequestEntityConverter());
        // 创建一个MappingJackson2HttpMessageConverter对象，设置支持的MediaType为text/plain
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));
        RestTemplate restTemplate = new RestTemplate(Collections.singletonList(messageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.setRestOperations(restTemplate);
    }

    // 对用户信息进行 SHA-256 哈希处理
    private static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes); // 将哈希结果转换为 Base64 字符串
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // 生成唯一用户名
    public static String generateUsername(String openid, String nickname) {
        // 对用户信息进行哈希处理
        String hashedInfo = hash(openid);

        // 截取哈希结果的前 8 个字符作为用户名后缀
        String suffix = hashedInfo.substring(0, 8);

        // 生成用户名（前缀 + 后缀）
        return nickname + "_" + suffix;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 获取第三方平台类型（如微信、Google）
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String uniqueId = oauth2User.getAttribute("openId"); // 或其他唯一标识字段


        //处理用户信息
        // 获取微信用户信息
        Map<String, Object> userAttributes = oauth2User.getAttributes();
        String openId = (String) userAttributes.get("openid"); // 微信用户的唯一标识
        String nickname = (String) userAttributes.get("nickname"); // 微信用户的昵称
        String avatar = (String) userAttributes.get("headimgurl"); // 微信用户的头像
        String userName = generateUsername(openId, nickname);
        //查找第三方用户
        Optional<ThirdPartUser> thirdPartyUser = thirdPartUserRepository.findByUniqueIdAndProviderId(uniqueId, registrationId);
        //查找本地用户
        User user = userRepository.findByUsername(userName);
        ThirdPartUser currenUser = null;
        if (thirdPartyUser.isPresent() && null!= user) {
            user = thirdPartyUser.get().getUser();
        } else if(!thirdPartyUser.isPresent() && null!=user) {
            currenUser = new ThirdPartUser();
            currenUser.setNickname(nickname);
            currenUser.setUser(user);
            currenUser.setUniqueId(openId);
            currenUser.setProviderId(registrationId);
            //关联user
            user.getThirdPartUsers().add(currenUser);
            saveUser(user);
        }else if (null == user && thirdPartyUser.isPresent()) {
            user = new User();
            user.setUsername(userName);
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setPassword(new BCryptPasswordEncoder().encode(defPassword));
            user.setId(StringExtUtils.getUuid());
            Set<String> roleId = new HashSet<>();
            roleId.add("平台游客");
            user.setRoleIds(roleId);
            currenUser = thirdPartyUser.get();
            //关联用户
            user.getThirdPartUsers().add(currenUser);
            saveUser(user);
        }else if (null==user && !thirdPartyUser.isPresent()){
            user = new User();
            user.setUsername(userName);
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setPassword(new BCryptPasswordEncoder().encode(defPassword));
            user.setId(StringExtUtils.getUuid());
            Set<String> roleId = new HashSet<>();
            roleId.add("平台游客");
            user.setRoleIds(roleId);
            currenUser = new ThirdPartUser();
            currenUser.setNickname(nickname);
            currenUser.setUser(user);
            currenUser.setUniqueId(openId);
            currenUser.setProviderId(registrationId);
            user.getThirdPartUsers().add(currenUser);
            saveUser(user);
        }

        return user;

    }

    public void saveUser(User localUser) {

        userRepository.save(localUser);
        roleUserRepository.saveAll(Arrays.stream(localUser.getRoleIds().toArray())
                .map(roleId -> new RoleUser().setId(StringExtUtils.getUuid())
                        .setRoleId((String) roleId)
                        .setUserId(localUser.getId())).collect(Collectors.toList()));
    }


}
