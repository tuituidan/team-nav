package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.repository.CategoryRepository;
import com.tuituidan.openhub.util.SecurityUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * CategoryService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
@CacheConfig(cacheNames = "categorys")
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private CardRepository cardRepository;

    /**
     * select
     *
     * @return List
     */
    public List<Category> select() {
        boolean isLogin = SecurityUtils.getUserInfo() != null;
        return categoryRepository.findAll(Example.of(new Category().setValid(true)),
                        Sort.by("sort")).stream()
                .filter(item -> isLogin || BooleanUtils.isNotTrue(item.getPrivateCard()))
                .collect(Collectors.toList());
    }

    /**
     * 分页查询无效分类
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<Category> selectPage(String keywords, Integer pageIndex, Integer pageSize) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",
                GenericPropertyMatchers.contains());
        return categoryRepository.findAll(Example.of(new Category().setValid(false).setName(keywords), matcher),
                PageRequest.of(pageIndex, pageSize, Sort.by("sort").descending()));
    }

    /**
     * get
     *
     * @param id id
     * @return Category
     */
    @Cacheable(key = "#id")
    public Category get(String id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    /**
     * 保存
     *
     * @param category category
     * @return category
     */
    @CachePut(key = "#result.id")
    public Category save(Category category) {
        if (StringUtils.isBlank(category.getId())) {
            category.setId(StringExtUtils.getUuid());
            category.setValid(true);
            category.setSort(categoryRepository.getMaxSort(true) + 1);
        }
        categoryRepository.save(category);
        return category;
    }

    /**
     * 删除
     *
     * @param id id
     */
    @CacheEvict(key = "#id")
    public void delete(String id) {
        categoryRepository.deleteById(id);
        cardRepository.deleteByCategoryId(id);
    }

    /**
     * 设为是否私密
     *
     * @param id id
     * @param privateCard privateCard
     */
    public void updatePrivate(String id, Boolean privateCard) {
        categoryRepository.updatePrivate(privateCard, Collections.singletonList(id));
    }

    /**
     * 修改排序
     *
     * @param before before
     * @param after after
     */
    public void changeSort(int before, int after) {
        if (before == after) {
            return;
        }
        List<Category> categories = categoryRepository.findAll(Example.of(
                new Category().setValid(true)), Sort.by("sort"));
        LinkedList<Category> list = new LinkedList<>(categories);
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        list.add(after, list.remove(before));
        List<Category> updateList = new ArrayList<>();
        int index = 0;
        for (Category item : list) {
            if (!Objects.equals(item.getSort(), index)) {
                updateList.add(item.setSort(index));
            }
            index++;
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            categoryRepository.saveAll(updateList);
        }
    }

    /**
     * 设置是否有效
     *
     * @param ids ids
     * @param valid valid
     */
    @Transactional(rollbackFor = Exception.class)
    public void setValid(List<String> ids, Boolean valid) {
        Assert.isTrue(CollectionUtils.isNotEmpty(ids), "ids不能为空");
        int sort = categoryRepository.getMaxSort(valid) + 1;
        List<Category> categories = categoryRepository.findAllById(ids);
        for (Category category : categories) {
            category.setValid(valid).setSort(sort);
            sort++;
        }
        if (CollectionUtils.isNotEmpty(categories)) {
            categoryRepository.saveAll(categories);
        }
        if (BooleanUtils.isNotTrue(valid)) {
            categoryRepository.updatePrivate(false, ids);
        }
    }

}
