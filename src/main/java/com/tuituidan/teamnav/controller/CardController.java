package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.bean.dto.CardDto;
import com.tuituidan.teamnav.bean.vo.CardTreeVo;
import com.tuituidan.teamnav.bean.vo.CardVo;
import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.service.CardService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CardController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@RestController
@RequestMapping(Consts.API_V1 + "/card")
public class CardController {

    @Resource
    private CardService cardService;

    @GetMapping("/tree")
    public ResponseEntity<List<CardTreeVo>> tree() {
        return ResponseEntity.ok(cardService.tree());
    }

    @GetMapping
    public ResponseEntity<List<CardVo>> select(@RequestParam("category") String category) {
        return ResponseEntity.ok(cardService.select(category));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CardDto cardDto) {
        cardService.save(cardDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody CardDto cardDto) {
        cardDto.setId(id);
        cardService.save(cardDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{direction}")
    public ResponseEntity<Void> changeSort(@PathVariable String id, @PathVariable String direction) {
        cardService.changeSort(id, direction);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
