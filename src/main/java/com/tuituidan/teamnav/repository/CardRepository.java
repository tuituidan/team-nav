package com.tuituidan.teamnav.repository;

import com.tuituidan.teamnav.bean.entity.Card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CardRepository.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
public interface CardRepository extends JpaRepository<Card, String> {

    /**
     * findByTag.
     *
     * @param category category
     * @return List
     */
    List<Card> findByCategoryOrderBySortAsc(String category);


    /**
     * findByTitleLikeOrContentLike.
     *
     * @param keyword1 keyword1
     * @param keyword2 keyword2
     * @return List
     */
    List<Card> findByTitleContainsOrContentContains(String keyword1, String keyword2);
}
