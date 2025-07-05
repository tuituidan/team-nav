package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.entity.SysDatasource;
import com.tuituidan.openhub.bean.vo.SysDatasourceVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.DatasourceService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NoticeController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/datasource")
public class DatasourceController {

    @Resource
    private DatasourceService datasourceService;

    /**
     * select
     *
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<SysDatasourceVo>> select() {
        return ResponseEntity.ok(datasourceService.select());
    }

    /**
     * add
     *
     * @param datasource datasource
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody SysDatasource datasource) {
        datasourceService.save(null, datasource);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param datasource datasource
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
            @RequestBody SysDatasource datasource) {
        datasourceService.save(id, datasource);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        datasourceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
