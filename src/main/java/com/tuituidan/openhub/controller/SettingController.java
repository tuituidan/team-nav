package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.Nginx;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.service.setting.SettingService;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SettingController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {

    @Resource
    private SettingService settingService;

    /**
     * 获取配置
     *
     * @return Setting
     */
    @GetMapping
    public ResponseEntity<Setting> get() {
        return ResponseEntity.ok(settingService.get());
    }

    /**
     * 保存Nginx配置
     *
     * @param nginx nginx
     * @return Void
     */
    @PutMapping("/nginx")
    public ResponseEntity<Void> saveNginx(@RequestBody Nginx nginx) {
        settingService.saveNginx(nginx);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
