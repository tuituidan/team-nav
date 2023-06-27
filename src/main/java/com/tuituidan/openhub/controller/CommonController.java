package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.LoginDto;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.CommonService;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 删除icon
     *
     * @param fileName fileName
     * @return Void
     * @throws IOException IOException
     */
    @DeleteMapping("/icon/{fileName}")
    public ResponseEntity<Void> deleteIcon(@PathVariable("fileName") String fileName) throws IOException {
        commonService.deleteDefaultIcon(fileName);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改icon文件名
     *
     * @param fileName fileName
     * @param newName newName
     * @return Void
     */
    @PatchMapping("/icon/{fileName}/to/{newName}")
    public ResponseEntity<Void> updateIconName(@PathVariable("fileName") String fileName,
            @PathVariable("newName") String newName) {
        commonService.updateIconName(fileName, newName);
        return ResponseEntity.noContent().build();
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

    /**
     * quickLogin
     *
     * @param loginDto loginDto
     * @return boolean
     */
    @PostMapping("/quick/login")
    public ResponseEntity<Boolean> quickLogin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(commonService.quickLogin(loginDto));
    }

}
