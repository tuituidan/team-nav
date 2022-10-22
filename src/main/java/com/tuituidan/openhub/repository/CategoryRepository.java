package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface CategoryRepository extends JpaRepository<Category, String> {

    /**
     * updatePrivate.
     *
     * @param privateCard privateCard
     * @param id id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Category set privateCard = ?1 where id in (?2)")
    void updatePrivate(Boolean privateCard, List<String> id);

    /**
     * getMaxSort
     *
     * @param valid valid
     * @return Integer
     */
    @Query("select coalesce(max(sort),0) from Category where valid = ?1")
    Integer getMaxSort(Boolean valid);

}
