package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CategoryDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.vo.CategoryVo;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.repository.CategoryRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.TransactionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private CardRepository cardRepository;

    @Resource
    private CommonService commonService;

    @Resource
    private RoleService roleService;

    @Resource
    private CacheService cacheService;

    /**
     * 查询列表
     *
     * @return List
     */
    public List<CategoryVo> select(String keywords) {
        Specification<Category> search = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.isNotBlank(keywords)) {
                predicate.getExpressions().add(builder.like(root.get("name"), "%" + keywords + "%"));
            }
            predicate.getExpressions().add(builder.isTrue(root.get("valid")));
            return predicate;
        };
        List<Category> categories = categoryRepository.findAll(search);
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        Map<String, Long> countMap = cardRepository.findByCategoryIn(categories.stream()
                        .map(Category::getId).collect(Collectors.toList())).stream()
                .collect(Collectors.groupingBy(Card::getCategory, Collectors.counting()));
        List<CategoryVo> categoryList = categories.stream()
                .map(item -> BeanExtUtils.convert(item, CategoryVo::new)
                        .setCardCount(countMap.getOrDefault(item.getId(), 0L))
                        .setRoles(cacheService.getRolesByCategoryId(item.getId()))
                )
                .collect(Collectors.toList());
        return ListUtils.buildTree(categoryList);
    }

    /**
     * selectTree
     *
     * @return List
     */
    public List<CategoryVo> selectTree(Integer level) {
        List<CategoryVo> categoryList = categoryRepository
                .findByValidTrueAndLevelLessThanEqual(level == null ? 3 : level).stream()
                .map(item -> BeanExtUtils.convert(item, CategoryVo::new))
                .collect(Collectors.toList());
        return ListUtils.buildTree(categoryList);
    }

    /**
     * 分页查询无效分类
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<CategoryVo> selectPage(String keywords, Integer pageIndex, Integer pageSize) {
        Specification<Category> search = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.isNotBlank(keywords)) {
                predicate.getExpressions().add(builder.like(root.get("name"), "%" + keywords + "%"));
            }
            predicate.getExpressions().add(builder.isFalse(root.get("valid")));
            return predicate;
        };
        Page<Category> page = categoryRepository.findAll(search,
                PageRequest.of(pageIndex, pageSize, Sort.by("updateTime").descending()));
        if (page.getTotalElements() <= 0) {
            return page.map(item -> new CategoryVo());
        }
        Map<String, Long> countMap = cardRepository.findByCategoryIn(page.getContent().stream()
                        .map(Category::getId).collect(Collectors.toList())).stream()
                .collect(Collectors.groupingBy(Card::getCategory, Collectors.counting()));
        return page.map(item -> {
            item.setName(buildCategoryName(item.getId()));
            CategoryVo vo = BeanExtUtils.convert(item, CategoryVo::new);
            vo.setCardCount(countMap.getOrDefault(item.getId(), 0L));
            vo.setRoles(cacheService.getRolesByCategoryId(item.getId()));
            return vo;
        });
    }

    /**
     * 构建级联分类名
     *
     * @param categoryId categoryId
     * @return String
     */
    public String buildCategoryName(String categoryId) {
        Category category = cacheService.getCategory(categoryId);
        if ("0".equals(category.getPid())) {
            return category.getName();
        }
        return buildCategoryName(category.getPid()) + " / " + category.getName();
    }

    /**
     * 保存
     *
     * @param id id
     * @param dto dto
     * @return category
     */
    public Category save(String id, CategoryDto dto) {
        Category category;
        if (StringUtils.isBlank(id)) {
            category = BeanExtUtils.convert(dto, Category::new);
            category.setId(StringExtUtils.getUuid());
            category.setValid(true);
            category.setSort(categoryRepository.getMaxSort(true) + 1);
        } else {
            category = categoryRepository.getReferenceById(id);
            BeanExtUtils.copyNotNullProperties(dto, category);
        }

        if ("0".equals(category.getPid())) {
            category.setLevel(1);
        } else {
            category.setLevel(cacheService.getCategory(category.getPid()).getLevel() + 1);
        }
        List<Card> cards = cardRepository.findByCategory(category.getPid());
        TransactionUtils.execute(() -> {
            categoryRepository.save(category);
            roleService.saveCategoryRoles(downCascade(new ArrayList<>(Collections.singletonList(category)))
                    .stream().map(Category::getId).collect(Collectors.toSet()), dto.getRoleIds());
            if (CollectionUtils.isNotEmpty(cards)) {
                cards.forEach(item -> item.setCategory(category.getId()));
                cardRepository.saveAll(cards);
            }
        });
        if (CollectionUtils.isNotEmpty(cards)) {
            cacheService.getCategoryCache().invalidate(category.getPid());
        }
        cacheService.getCategoryCache().invalidate(category.getId());
        return category;
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String id) {
        List<Category> children = categoryRepository.findByPid(id);
        Assert.isTrue(CollectionUtils.isEmpty(children), "存在子分类，不允许删除");
        categoryRepository.deleteById(id);
        cardRepository.deleteByCategory(id);
        cacheService.getCategoryCache().invalidate(id);
    }

    /**
     * 修改排序
     *
     * @param before before
     * @param after after
     */
    public void changeSort(int before, int after) {
        Supplier<List<Category>> supplier = () -> categoryRepository.findByValidTrueOrderBySort();
        List<Category> updateList = commonService.changeSort(supplier, before, after);
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        categoryRepository.saveAll(updateList);
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
        List<Category> categories = BooleanUtils.isTrue(valid)
                ? upCascade(categoryRepository.findAllById(ids))
                : downCascade(categoryRepository.findAllById(ids));
        for (Category category : categories) {
            category.setValid(valid).setSort(sort);
            sort++;
        }
        if (CollectionUtils.isNotEmpty(categories)) {
            categoryRepository.saveAll(categories);
        }
    }

    private List<Category> downCascade(List<Category> categories) {
        List<Category> children = categoryRepository.findByPidIn(categories.stream()
                .map(Category::getId).collect(Collectors.toSet()));
        if (CollectionUtils.isEmpty(children)) {
            return categories;
        }
        categories.addAll(downCascade(children));
        return categories;
    }

    private List<Category> upCascade(List<Category> categories) {
        List<Category> parents = categoryRepository.findAllById(categories.stream()
                .map(Category::getPid).collect(Collectors.toSet()));
        if (CollectionUtils.isEmpty(parents)) {
            return categories;
        }
        categories.addAll(upCascade(parents));
        return categories;
    }

}
