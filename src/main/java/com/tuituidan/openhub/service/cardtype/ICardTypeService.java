package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.entity.Card;

/**
 * 类型接口，方便扩展.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
public interface ICardTypeService {

    /**
     * format
     *
     * @param card card
     * @param cardDto cardDto
     */
    void format(Card card, CardDto cardDto);

}
