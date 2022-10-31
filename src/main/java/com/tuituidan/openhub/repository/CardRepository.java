package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * CardRepository.
 *
 * @author tuituidan
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
     * @param keyword keyword
     * @return List
     */
    @Query("select u from Card u "
            + "where lower(u.title) like %?1% or lower(u.content) like %?1%")
    List<Card> findByKeywords(String keyword);

    /**
     * getMaxSort
     *
     * @param category category
     * @return Integer
     */
    @Query("select coalesce(max(sort),0) from Card where category = ?1")
    Integer getMaxSort(String category);

    /**
     * deleteByCategory
     *
     * @param categoryId categoryId
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from Card where category = ?1")
    void deleteByCategoryId(String categoryId);

}
