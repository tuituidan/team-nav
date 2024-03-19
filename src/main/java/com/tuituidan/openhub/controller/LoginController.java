package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.LoginDto;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.config.LoginSuccessHandler;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.UserService;
import com.tuituidan.openhub.util.RequestUtils;
import com.tuituidan.openhub.util.ResponseUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/7/7
 */
@RestController
@RequestMapping(Consts.API_V1)
public class LoginController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private UserService userService;

    /**
     * quickLogin
     *
     * @param loginDto loginDto
     * @return boolean
     */
    @PostMapping("/quick/login")
    public ResponseEntity<User> quickLogin(@RequestBody LoginDto loginDto) throws ServletException, IOException {
        if (SecurityUtils.getUserInfo() != null) {
            return ResponseEntity.ok(SecurityUtils.getUserInfo());
        }
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        Assert.notNull(authenticate, "登录失败");
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        if (loginDto.getCardIds() != null) {
            User userInfo = SecurityUtils.getUserInfo();
            userService.userStarCard(userInfo.getId(), loginDto.getCardIds());
        }
        loginSuccessHandler.onAuthenticationSuccess(RequestUtils.getRequest(), ResponseUtils.getHttpResponse(),
                authenticate);
        return ResponseEntity.ok(SecurityUtils.getUserInfo());
    }

    /**
     * getUser
     *
     * @return User
     */
    @GetMapping("/login/user")
    public User getUser() {
        return SecurityUtils.getUserInfo();
    }

}
