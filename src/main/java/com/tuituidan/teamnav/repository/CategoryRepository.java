package com.tuituidan.teamnav.repository;

import com.tuituidan.teamnav.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CategoryRepository.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
public interface CategoryRepository extends JpaRepository<Category, String> {
}
