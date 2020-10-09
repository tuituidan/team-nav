package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.entity.Tag;
import com.tuituidan.teamnav.service.TagService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * TagController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/tag")
@Api(tags = "分类")
public class TagController {

    @Resource
    private TagService tagService;

    @ApiOperation("列表查询")
    @GetMapping
    public ResponseEntity<List<Tag>> select() {
        return ResponseEntity.ok(tagService.select());
    }

    @ApiOperation("添加分类")
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("修改分类")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @RequestBody Tag tag) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.noContent().build();
    }
}
