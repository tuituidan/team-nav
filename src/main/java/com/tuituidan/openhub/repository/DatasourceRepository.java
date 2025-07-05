package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.SysDatasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * SysDatasourceRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface DatasourceRepository extends JpaRepository<SysDatasource, String>,
        JpaSpecificationExecutor<SysDatasource> {

}
