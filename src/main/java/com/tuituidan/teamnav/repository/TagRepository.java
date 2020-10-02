package com.tuituidan.teamnav.repository;

import com.tuituidan.teamnav.entity.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TagRepository.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
