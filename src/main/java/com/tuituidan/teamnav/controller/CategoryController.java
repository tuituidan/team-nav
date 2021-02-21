package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.entity.Category;
import com.tuituidan.teamnav.service.CategoryService;

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
 * CategoryController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/tag")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> select() {
        return ResponseEntity.ok(categoryService.select());
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Category tag) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @RequestBody Category tag) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.noContent().build();
    }
}
