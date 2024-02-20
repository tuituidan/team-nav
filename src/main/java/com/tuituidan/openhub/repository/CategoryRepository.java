package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Category;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * CategoryRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {

    /**
     * findByPid
     *
     * @param pid pid
     * @return List
     */
    List<Category> findByPid(String pid);

    /**
     * findByPid
     *
     * @param pids pids
     * @return List
     */
    List<Category> findByPidIn(Collection<String> pids);

    /**
     * getMaxSort
     *
     * @param valid valid
     * @return Integer
     */
    @Query("select coalesce(max(sort),0) from Category where valid = ?1")
    Integer getMaxSort(Boolean valid);

    /**
     * 根据级别查询
     *
     * @param level level
     * @return List
     */
    List<Category> findByValidTrueAndLevelLessThanEqual(Integer level);

    /**
     * findByValidTrue
     *
     * @return List
     */
    List<Category> findByValidTrueOrderBySort();

    /**
     * findByPidAndValidTrueOrderBySort
     *
     * @param pid pid
     * @return List
     */
    List<Category> findByPidAndValidTrueOrderBySort(String pid);

}
