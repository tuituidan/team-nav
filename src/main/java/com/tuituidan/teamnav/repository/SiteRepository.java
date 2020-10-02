package com.tuituidan.teamnav.repository;

import com.tuituidan.teamnav.entity.Site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SiteRepository.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
public interface SiteRepository extends JpaRepository<Site, Integer> {

    /**
     * findByTag
     *
     * @param tag tag
     * @return List
     */
    List<Site> findByTag(String tag);

}
