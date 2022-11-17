package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.ChangePassword;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.repository.UserRepository;
import com.tuituidan.openhub.util.SecurityUtils;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * UserService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/16
 */
@Service
public class UserService implements UserDetailsService, ApplicationRunner {

    @Resource
    private UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = get();
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            // 加载默认配置，兼容老版本
            user.setNickname(SecurityUtils.DEFAULT_USER.getNickname());
            user.setAvatar(SecurityUtils.DEFAULT_USER.getAvatar());
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = get();
        Assert.isTrue(StringUtils.equals(username, user.getUsername()), "用户名或密码错误");
        return user;
    }

    /**
     * changePassword
     *
     * @param changePassword changePassword
     */
    public void changePassword(ChangePassword changePassword) {
        User user = get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Assert.isTrue(passwordEncoder.matches(changePassword.getOldPassword(),
                user.getPassword()), "原密码不匹配");
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
    }

    private User get() {
        return userRepository.findAll().stream().findFirst()
                .orElse(new User().setId("1"));
    }

}
