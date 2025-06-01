package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.RoleUserDto;
import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.bean.entity.RoleCategory;
import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.bean.vo.RoleVo;
import com.tuituidan.openhub.repository.RoleCategoryRepository;
import com.tuituidan.openhub.repository.RoleRepository;
import com.tuituidan.openhub.repository.RoleUserRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * RoleService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/2
 */
@Service
public class RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private RoleCategoryRepository roleCategoryRepository;

    @Resource
    private RoleUserRepository roleUserRepository;

    @Resource
    private CacheService cacheService;

    /**
     * select
     *
     * @return List
     */
    public List<Role> select() {
        return roleRepository.findAll(Sort.by("updateTime").descending());
    }

    /**
     * 分页查询用户
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<Role> selectPage(String keywords, Integer pageIndex, Integer pageSize) {
        Specification<Role> search = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.isNotBlank(keywords)) {
                predicate.getExpressions().add(builder.like(root.get("roleName"), "%" + keywords + "%"));
            }
            return predicate;
        };
        return roleRepository.findAll(search, PageRequest.of(pageIndex, pageSize, Sort.by("updateTime")));
    }

    /**
     * getRoleDetail
     *
     * @param id id
     * @return RoleVo
     */
    public RoleVo getRoleDetail(String id) {
        RoleVo role = BeanExtUtils.convert(roleRepository.findById(id)
                .orElseThrow(NullPointerException::new), RoleVo::new);
        role.setCategoryIds(roleCategoryRepository.findByRoleId(id).stream()
                .map(RoleCategory::getCategoryId).distinct().collect(Collectors.toList()));
        role.setUserIds(roleUserRepository.findByRoleId(id).stream()
                .map(RoleUser::getUserId).distinct().collect(Collectors.toList()));
        return role;
    }

    /**
     * 保存
     *
     * @param id id
     * @param roleName roleName
     */
    public void save(String id, String roleName) {
        Role role = new Role().setId(id).setRoleName(roleName);
        if (StringUtils.isBlank(id)) {
            role.setId(StringExtUtils.getUuid());
        }
        roleRepository.save(role);
        cacheService.getRoleCache().invalidate(role.getId());
    }

    /**
     * 保存分类和角色映射关系
     *
     * @param categoryIds categoryIds
     * @param roleIds roleIds
     */
    public void saveCategoryRoles(Set<String> categoryIds, String[] roleIds) {
        roleCategoryRepository.deleteByCategoryIdIn(categoryIds);
        List<RoleCategory> saveList = new ArrayList<>();
        for (String categoryId : categoryIds) {
            for (String roleId : roleIds) {
                saveList.add(new RoleCategory().setId(StringExtUtils.getUuid())
                        .setCategoryId(categoryId).setRoleId(roleId));
            }
        }
        roleCategoryRepository.saveAll(saveList);
        cacheService.getCategoryRolesCache().invalidateAll(categoryIds);
    }

    /**
     * saveRoleCategories
     *
     * @param roleId roleId
     * @param categoryIds categoryIds
     */
    public void saveRoleCategories(String roleId, String[] categoryIds) {
        roleCategoryRepository.deleteByRoleId(roleId);
        if (ArrayUtils.isNotEmpty(categoryIds)) {
            roleCategoryRepository.saveAll(Arrays.stream(categoryIds)
                    .map(categoryId -> new RoleCategory()
                            .setId(StringExtUtils.getUuid())
                            .setCategoryId(categoryId)
                            .setRoleId(roleId))
                    .collect(Collectors.toList()));
        }
        cacheService.getCategoryRolesCache().invalidateAll(Arrays.stream(categoryIds)
                .collect(Collectors.toSet()));
    }

    /**
     * saveRoleUser
     *
     * @param roleId roleId
     * @param roleUserDto roleUserDto
     */
    public void saveRoleUser(String roleId, RoleUserDto roleUserDto) {
        roleUserRepository.deleteByRoleIdAndUserIdIn(roleId, Arrays.asList(roleUserDto.getUserIds()));
        if (ArrayUtils.isNotEmpty(roleUserDto.getCheckIds())) {
            roleUserRepository.saveAll(Arrays.stream(roleUserDto.getCheckIds())
                    .map(checkId -> new RoleUser()
                            .setId(StringExtUtils.getUuid())
                            .setUserId(checkId)
                            .setRoleId(roleId))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String id) {
        List<RoleCategory> categories = roleCategoryRepository.findByRoleId(id);
        Assert.isTrue(CollectionUtils.isEmpty(categories), "该角色下存在卡片分类，不能删除");
        List<RoleUser> users = roleUserRepository.findByRoleId(id);
        Assert.isTrue(CollectionUtils.isEmpty(users), "该角色下存在用户，不能删除");
        roleRepository.deleteById(id);
        cacheService.getRoleCache().invalidate(id);
    }

}
