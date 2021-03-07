package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.bean.entity.Category;
import com.tuituidan.teamnav.consts.Consts;
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
@RequestMapping(Consts.API_V1 + "/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> select() {
        return ResponseEntity.ok(categoryService.select());
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Category category) {
        category.setId(id);
        categoryService.save(category);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{direction}")
    public ResponseEntity<Void> changeSort(@PathVariable String id, @PathVariable String direction) {
        categoryService.changeSort(id, direction);
        return ResponseEntity.noContent().build();
    }
}
