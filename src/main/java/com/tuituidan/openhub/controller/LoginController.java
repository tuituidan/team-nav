package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.LoginDto;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.service.CommonService;
import com.tuituidan.openhub.util.SecurityUtils;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/7/7
 */
@RestController
public class LoginController {

    @Resource
    private CommonService commonService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        boolean result = commonService.quickLogin(loginDto);
        return loginDto.getUsername();
    }

    @GetMapping("getInfo")
    public User getUser() {
        return SecurityUtils.getUserInfo();
    }

}
