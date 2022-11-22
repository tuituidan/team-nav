package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.CountdownDto;
import com.tuituidan.openhub.bean.vo.CountdownVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.service.CountdownService;
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
 * CountdownController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/countdown")
public class CountdownController {

    @Resource
    private CountdownService countdownService;

    /**
     * select
     *
     * @param status status
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<CountdownVo>> select(Boolean status) {
        return ResponseEntity.ok(countdownService.select(status));
    }

    /**
     * add
     *
     * @param countdown countdown
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CountdownDto countdown) {
        countdownService.save(null, countdown);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param countdown countdown
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
            @RequestBody CountdownDto countdown) {
        countdownService.save(id, countdown);
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
        countdownService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
