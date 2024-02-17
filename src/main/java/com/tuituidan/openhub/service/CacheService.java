package com.tuituidan.openhub.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.bean.entity.RoleCategory;
import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.repository.CategoryRepository;
import com.tuituidan.openhub.repository.RoleCategoryRepository;
import com.tuituidan.openhub.repository.RoleRepository;
import com.tuituidan.openhub.repository.RoleUserRepository;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * CacheService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/14
 */
@Service
@Getter
public class CacheService implements ApplicationRunner {

    private final Cache<String, Category> categoryCache = Caffeine.newBuilder().build();

    private final Cache<String, Role> roleCache = Caffeine.newBuilder().build();

    private final Cache<String, List<Role>> categoryRolesCache = Caffeine.newBuilder().build();

    private final Cache<String, List<Category>> roleCategoriesCache = Caffeine.newBuilder().build();

    private final Cache<String, List<Role>> userRolesCache = Caffeine.newBuilder().build();

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private RoleCategoryRepository roleCategoryRepository;

    @Resource
    private RoleUserRepository roleUserRepository;

    /**
     * getCategory
     *
     * @param id id
     * @return Category
     */
    public Category getCategory(String id) {
        return categoryCache.get(id, key -> categoryRepository.getReferenceById(key));
    }

    /**
     * getRole
     *
     * @param id id
     * @return Role
     */
    public Role getRole(String id) {
        return roleCache.get(id, key -> roleRepository.getReferenceById(key));
    }

    /**
     * getRolesByCategoryId
     *
     * @param categoryId categoryId
     * @return List
     */
    public List<Role> getRolesByCategoryId(String categoryId) {
        return categoryRolesCache.get(categoryId, id ->
                roleCategoryRepository.findByCategoryId(id).stream()
                        .map(item -> this.getRole(item.getRoleId()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * getRolesByCategoryId
     *
     * @param userId userId
     * @return List
     */
    public List<Role> getRolesByUserId(String userId) {
        return userRolesCache.get(userId, id ->
                roleUserRepository.findByUserId(id).stream()
                        .map(item -> this.getRole(item.getRoleId()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadCategoryCache();
        loadRoleCache();
        loadCategoryToRoleCache();
        loadUserRolesCache();
    }

    private void loadCategoryCache() {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            categoryCache.put(category.getId(), category);
        }
    }

    private void loadRoleCache() {
        List<Role> roles = roleRepository.findAll();
        roleCache.invalidateAll();
        roles.forEach(item -> roleCache.put(item.getId(), item));
    }

    private void loadCategoryToRoleCache() {
        List<RoleCategory> roleCategories = roleCategoryRepository.findAll();
        Map<String, List<String>> categoryRoleIdsMap = roleCategories.stream()
                .collect(Collectors.groupingBy(RoleCategory::getCategoryId,
                        Collectors.mapping(RoleCategory::getRoleId, Collectors.toList())));
        categoryRolesCache.invalidateAll();
        for (Entry<String, List<String>> entry : categoryRoleIdsMap.entrySet()) {
            categoryRolesCache.put(entry.getKey(), entry.getValue().stream()
                    .map(this::getRole).collect(Collectors.toList()));
        }
    }

    private void loadUserRolesCache() {
        List<RoleUser> roleUsers = roleUserRepository.findAll();
        Map<String, List<String>> userRoleIdsMap = roleUsers.stream()
                .collect(Collectors.groupingBy(RoleUser::getUserId,
                        Collectors.mapping(RoleUser::getRoleId, Collectors.toList())));
        userRolesCache.invalidateAll();
        for (Entry<String, List<String>> entry : userRoleIdsMap.entrySet()) {
            userRolesCache.put(entry.getKey(), entry.getValue().stream()
                    .map(this::getRole).collect(Collectors.toList()));
        }
    }

}
