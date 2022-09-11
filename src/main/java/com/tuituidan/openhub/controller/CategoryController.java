package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.CategoryService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
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
 * CategoryController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询有效列表
     *
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<Category>> select() {
        return ResponseEntity.ok(categoryService.select());
    }

    /**
     * 分页查询无效分类
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Category>> selectPage(String keywords,
            @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(categoryService.selectPage(keywords, pageIndex, pageSize));
    }

    /**
     * add
     *
     * @param category category
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param category category
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Category category) {
        category.setId(id);
        categoryService.save(category);
        return ResponseEntity.noContent().build();
    }

    /**
     * update private
     *
     * @param id id
     * @param privateCard privateCard
     * @return Void
     */
    @PatchMapping("/{id}/private/{type}")
    public ResponseEntity<Void> updatePrivate(@PathVariable("id") String id,
            @PathVariable("type") Boolean privateCard) {
        categoryService.updatePrivate(id, privateCard);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * changeSort
     *
     * @param before before
     * @param after after
     * @return Void
     */
    @PatchMapping("/actions/sort")
    public ResponseEntity<Void> changeSort(@RequestParam Integer before, @RequestParam Integer after) {
        categoryService.changeSort(before, after);
        return ResponseEntity.noContent().build();
    }

    /**
     * setValid
     *
     * @param id id
     * @param valid valid
     * @return Void
     */
    @PatchMapping("/{id}/valid/{valid}")
    public ResponseEntity<Void> setValid(@PathVariable String id, @PathVariable Boolean valid) {
        categoryService.setValid(id, valid);
        return ResponseEntity.noContent().build();
    }

}
