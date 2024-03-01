package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CategoryDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.vo.CategoryVo;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.repository.CategoryRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.TransactionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        List<CategoryVo> categoryList = getCategoryByLoginUser().stream()
                .filter(item -> item.getLevel() <= (level == null ? 3 : level))
                .collect(Collectors.toList());
        return ListUtils.buildTree(categoryList);
    }

    /**
     * getCategoryByLoginUser
     *
     * @return List
     */
    public List<CategoryVo> getCategoryByLoginUser() {
        List<Category> categories = categoryRepository.findByValidTrue();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        User userInfo = SecurityUtils.getUserInfo();
        if (SecurityUtils.isAdmin(userInfo)) {
            return categories.stream()
                    .map(item -> BeanExtUtils.convert(item, CategoryVo::new))
                    .collect(Collectors.toList());
        }
        List<CategoryVo> list = categories.stream()
                .map(item -> BeanExtUtils.convert(item, CategoryVo::new)
                        .setRoleIds(cacheService.getRolesByCategoryId(item.getId())
                                .stream().map(Role::getId).collect(Collectors.toSet())))
                .collect(Collectors.toList());
        if (userInfo == null) {
            return list.stream().filter(item -> CollectionUtils.isEmpty(item.getRoleIds()))
                    .collect(Collectors.toList());
        }
        return list.stream().filter(item ->
                CollectionUtils.isEmpty(item.getRoleIds())
                        || CollectionUtils.containsAny(item.getRoleIds(), userInfo.getRoleIds())
        ).collect(Collectors.toList());
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
            category.setSort(categoryRepository.getMaxSort(category.getId(), category.getPid(), true) + 1);
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
        cacheService.getCategoryRolesCache().invalidate(category.getId());
        return category;
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String[] id) {
        List<String> ids = Arrays.asList(id);
        List<Category> children = categoryRepository.findByPidIn(ids);
        Assert.isTrue(CollectionUtils.isEmpty(children), "要删除的分类存在子分类，不允许删除");
        TransactionUtils.execute(() -> {
            categoryRepository.deleteAllById(ids);
            cardRepository.deleteByCategoryIn(ids);
        });
        cacheService.getCategoryCache().invalidateAll(ids);
    }

    /**
     * 修改排序
     *
     * @param sortDto sortDto
     */
    public void changeSort(SortDto sortDto) {
        List<Category> categories =
                categoryRepository.findByPidAndValidTrueOrderBySort(sortDto.getParentId());
        List<Category> saveList = ListUtils.changeSort(categories, sortDto);
        if (CollectionUtils.isEmpty(saveList)) {
            return;
        }
        categoryRepository.saveAll(saveList);
        cacheService.getCategoryCache().invalidateAll(saveList.stream()
                .map(Category::getId).collect(Collectors.toSet()));
    }

    /**
     * 设置是否有效
     *
     * @param ids ids
     * @param valid valid
     */
    public void setValid(List<String> ids, Boolean valid) {
        Assert.isTrue(CollectionUtils.isNotEmpty(ids), "ids不能为空");
        List<Category> categories = BooleanUtils.isTrue(valid)
                ? upCascade(categoryRepository.findAllById(ids))
                : downCascade(categoryRepository.findAllById(ids));
        categories = categories.stream()
                .filter(item -> !Objects.equals(valid, item.getValid()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categories)) {
            return;
        }
        Map<String, Integer> sortMap = new HashMap<>();
        for (Category category : categories) {
            category.setValid(valid);
            Integer sort;
            if (sortMap.containsKey(category.getPid())) {
                sort = sortMap.get(category.getPid());
            } else {
                sort = categoryRepository.getMaxSort(category.getId(), category.getPid(), valid) + 1;
            }
            category.setSort(sort);
            sortMap.put(category.getPid(), sort + 1);
        }
        if (CollectionUtils.isNotEmpty(categories)) {
            categoryRepository.flush();
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
