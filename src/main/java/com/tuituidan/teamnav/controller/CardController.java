package com.tuituidan.teamnav.controller;

import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.entity.Card;
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
@RequestMapping(Consts.API_V1 +"/card")
public class CardController {

    @Resource
    private CardService cardService;

    @GetMapping
    public ResponseEntity<List<Card>> select(@RequestParam("card") String card) {

        return ResponseEntity.ok(cardService.select(card));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Card site) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Card site) {

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        return ResponseEntity.noContent().build();
    }
}
