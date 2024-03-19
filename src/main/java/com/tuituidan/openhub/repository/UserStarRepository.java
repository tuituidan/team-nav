package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.UserStar;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserStarRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/3/3
 */
public interface UserStarRepository extends JpaRepository<UserStar, String> {

    /**
     * findByUserId
     *
     * @param userId userId
     * @return List
     */
    List<UserStar> findByUserId(String userId);

    /**
     * findByUserIdAndCardId
     *
     * @param userId userId
     * @param cardId cardId
     * @return List
     */
    List<UserStar> findByUserIdAndCardId(String userId, String cardId);

    /**
     * deleteByUserIdAndCardId
     *
     * @param userId userId
     * @param cardId cardId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserIdAndCardId(String userId, String cardId);

    /**
     * deleteByUserIdAndCardId
     *
     * @param userId userId
     * @param cardIds cardIds
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserIdAndCardIdIn(String userId, Collection<String> cardIds);

}
