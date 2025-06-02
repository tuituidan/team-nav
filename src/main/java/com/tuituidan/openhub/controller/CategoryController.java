package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.CategoryDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.vo.BookmarkVo;
import com.tuituidan.openhub.bean.vo.CategoryVo;
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
     * @param keywords keywords
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<CategoryVo>> select(String keywords) {
        return ResponseEntity.ok(categoryService.select(keywords));
    }

    /**
     * 查询树结构
     *
     * @param level 级别
     * @return List
     */
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryVo>> selectTree(
            @RequestParam(required = false) Integer level) {
        return ResponseEntity.ok(categoryService.selectTree(level));
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
    public ResponseEntity<Page<CategoryVo>> selectPage(String keywords,
            @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(categoryService.selectPage(keywords, pageIndex - 1, pageSize));
    }

    /**
     * add
     *
     * @param category category
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CategoryDto category) {
        categoryService.save(null, category);
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
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody CategoryDto category) {
        categoryService.save(id, category);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String[] id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * changeSort
     *
     * @param sortDto sortDto
     * @return Void
     */
    @PatchMapping("/actions/sort")
    public ResponseEntity<Void> changeSort(@RequestBody SortDto sortDto) {
        categoryService.changeSort(sortDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * setValid
     *
     * @param ids ids
     * @param valid valid
     * @return Void
     */
    @PatchMapping("/valid/{valid}")
    public ResponseEntity<Void> setValid(@RequestBody List<String> ids, @PathVariable Boolean valid) {
        categoryService.setValid(ids, valid);
        return ResponseEntity.noContent().build();
    }

    /**
     * importBookmark
     *
     * @param datas datas
     * @return Void
     */
    @PostMapping("/bookmark/actions/import")
    public ResponseEntity<Void> importBookmark(@RequestBody List<BookmarkVo> datas) {
        categoryService.importBookmark(datas);
        return ResponseEntity.noContent().build();
    }

}
