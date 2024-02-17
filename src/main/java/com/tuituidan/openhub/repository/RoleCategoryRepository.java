package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.RoleCategory;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface RoleCategoryRepository extends JpaRepository<RoleCategory, String> {

    /**
     * deleteByCategoryIdIn
     *
     * @param categoryIds categoryIds
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByCategoryIdIn(Collection<String> categoryIds);

    /**
     * deleteByRoleId
     *
     * @param roleId roleId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByRoleId(String roleId);

    /**
     * findByRoleId
     *
     * @param roleId roleId
     * @return List
     */
    List<RoleCategory> findByRoleId(String roleId);

    /**
     * findByCategoryId
     *
     * @param categoryId categoryId
     * @return List
     */
    List<RoleCategory> findByCategoryId(String categoryId);

}
