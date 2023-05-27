package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * NoticeRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface NoticeRepository extends JpaRepository<Notice, String> {

    /**
     * getMaxSort
     *
     * @return Integer
     */
    @Query("select coalesce(max(sort),0) from Notice")
    Integer getMaxSort();
}
