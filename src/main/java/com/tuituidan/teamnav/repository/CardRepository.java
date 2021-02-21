package com.tuituidan.teamnav.repository;

import com.tuituidan.teamnav.entity.Card;

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
    List<Card> findByCategory(String category);

}
