package com.tuituidan.teamnav.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.repository.CategoryRepository;
import com.tuituidan.teamnav.service.CommonService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private CategoryRepository categoryRepository;

    @Resource
    private CommonService commonService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFile file) {
        return ResponseEntity.ok(commonService.upload(file));
    }

    @PostMapping("/upload/zip")
    public ResponseEntity<String> uploadZip(MultipartFile file) {
        return ResponseEntity.ok(commonService.uploadZip(file));
    }

    @GetMapping("/color")
    public ResponseEntity<String> getColor() {
        return ResponseEntity.ok(commonService.getColor());
    }

    @GetMapping("/category/icons")
    public ResponseEntity<List<String>> categoryIcons() {
        return ResponseEntity.ok(commonService.categoryIcons());
    }

    @GetMapping("/card/icons")
    public ResponseEntity<List<String>> cardIcons(String url) {
        return ResponseEntity.ok(commonService.cardIcons(url));
    }

    @GetMapping("/backup")
    public ResponseEntity<String> backup() {
        return ResponseEntity.ok(JSON.toJSONString(categoryRepository.findAll(), SerializerFeature.PrettyFormat));
    }
}
