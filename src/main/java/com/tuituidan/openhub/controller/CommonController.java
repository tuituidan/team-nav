package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.CommonService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * CommonController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/21
 */
@RestController
@RequestMapping(Consts.API_V1)
public class CommonController {

    @Resource
    private CommonService commonService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @param type 文件类型
     * @return 保存路径
     */
    @PostMapping("/upload/{type}")
    public ResponseEntity<String> upload(@PathVariable("type") String type, MultipartFile file) {
        return ResponseEntity.ok(commonService.upload(file, type));
    }

    /**
     * 获取分类的图标
     *
     * @return List
     */
    @GetMapping("/category/icons")
    public ResponseEntity<List<String>> categoryIcons() {
        return ResponseEntity.ok(commonService.categoryIcons());
    }

    /**
     * 获取分类的图标
     *
     * @return List
     */
    @GetMapping("/card/icons")
    public ResponseEntity<List<String>> cardIcons() {
        return ResponseEntity.ok(commonService.cardIcons());
    }

    /**
     * 获取卡片对应链接的 favicon.ico 用于icon
     *
     * @param url url
     * @return List
     */
    @GetMapping("/card/icon")
    public ResponseEntity<List<String>> cardIcons(String url) {
        return ResponseEntity.ok(commonService.cardIcons(url));
    }

    /**
     * generateQrCode
     *
     * @param url url
     */
    @GetMapping("/qrcode")
    public void generateQrCode(String url) {
        commonService.generateQrCode(url);
    }

}
