package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.bean.entity.Category;
import com.tuituidan.teamnav.repository.CategoryRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "categorys")
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    public List<Category> select() {
        return categoryRepository.findAll(Sort.by("sort"));
    }

    @Cacheable(key = "#id")
    public Category get(String id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    @CachePut(key = "#result.id")
    public Category save(Category category) {
        if (StringUtils.isBlank(category.getId())) {
            Integer maxSort = categoryRepository.findAll().stream().max(Comparator.comparing(Category::getSort))
                    .map(Category::getSort).orElse(0);
            category.setSort(maxSort + 1);
        }
        categoryRepository.save(category);
        return category;
    }

    @CacheEvict(key = "#id")
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }

    public void changeSort(String id, String direction) {
        List<Category> list = categoryRepository.findAll(Sort.by(Sort.Direction.fromString(direction), "sort"));
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        int index = 0;
        for (Category item : list) {
            if (item.getId().equals(id)) {
                Category last = list.get(index - 1);
                Integer lastSort = last.getSort();
                last.setSort(item.getSort());
                item.setSort(lastSort);
                categoryRepository.saveAll(Arrays.asList(last, item));
                break;
            }
            index++;
        }
    }
}
