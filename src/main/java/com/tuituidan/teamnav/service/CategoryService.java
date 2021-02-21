package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.entity.Category;
import com.tuituidan.teamnav.repository.CategoryRepository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * CategoryService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    public List<Category> select() {
        return categoryRepository.findAll(Sort.by("sort"));
    }

    public void save(Category tag) {
        categoryRepository.save(tag);
    }

    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}
