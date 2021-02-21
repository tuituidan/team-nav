package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.entity.Card;
import com.tuituidan.teamnav.repository.CardRepository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * CardService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class CardService {

    @Resource
    private CardRepository cardRepository;

    public List<Card> select(String category) {
        return cardRepository.findByCategory(category);
    }

    public void save(){

    }

}
