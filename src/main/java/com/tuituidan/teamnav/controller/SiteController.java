package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.entity.Site;
import com.tuituidan.teamnav.service.SiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SiteController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Api(tags = "网址")
@RestController
@RequestMapping(Consts.API_V1 +"/site")
public class SiteController {

    @Resource
    private SiteService siteService;

    @ApiOperation("列表查询")
    @GetMapping
    public ResponseEntity<List<Site>> select(@RequestParam("tag") String tag) {

        return ResponseEntity.ok(siteService.select(tag));
    }

    @ApiOperation("新增网址")
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Site site) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("修改网址")
    @PatchMapping
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @RequestBody Site site) {

        return ResponseEntity.noContent().build();
    }

    @ApiOperation("删除网址")
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.noContent().build();
    }
}
