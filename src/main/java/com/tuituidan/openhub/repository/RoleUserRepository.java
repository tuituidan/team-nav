package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.RoleUser;
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
public interface RoleUserRepository extends JpaRepository<RoleUser, String> {

    /**
     * deleteByRoleIdAndUserIdIn
     *
     * @param roleId roleId
     * @param userIds userIds
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByRoleIdAndUserIdIn(String roleId, Collection<String> userIds);

    /**
     * deleteByUserId
     *
     * @param userIds userIds
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserIdIn(Collection<String> userIds);

    /**
     * deleteByUserId
     *
     * @param userId userId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(String userId);

    /**
     * findByUserId
     *
     * @param userId userId
     * @return List
     */
    List<RoleUser> findByUserId(String userId);

    /**
     * findByUserIdIn
     *
     * @param userIds userIds
     * @return List
     */
    List<RoleUser> findByUserIdIn(Collection<String> userIds);

    /**
     * findByRoleId
     *
     * @param roleId roleId
     * @return List
     */
    List<RoleUser> findByRoleId(String roleId);

}
