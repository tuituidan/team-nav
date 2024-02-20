package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.NoticeDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.vo.NoticeVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.NoticeService;
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
@RequestMapping(Consts.API_V1 + "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * select
     *
     * @param status status
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<NoticeVo>> select(Boolean status) {
        return ResponseEntity.ok(noticeService.select(status));
    }

    /**
     * add
     *
     * @param noticeDto noticeDto
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody NoticeDto noticeDto) {
        noticeService.save(null, noticeDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param noticeDto noticeDto
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
            @RequestBody NoticeDto noticeDto) {
        noticeService.save(id, noticeDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * changeSort
     *
     * @param sortDto sortDto
     * @return Void
     */
    @PatchMapping("/actions/sort")
    public ResponseEntity<Void> changeSort(@RequestBody SortDto sortDto) {
        noticeService.changeSort(sortDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String[] id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
