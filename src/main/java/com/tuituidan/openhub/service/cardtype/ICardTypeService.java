package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.CardTreeChildVo;

/**
 * 类型接口，方便扩展.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
public interface ICardTypeService {

    /**
     * formatCard
     *
     * @param cardVo cardVo
     */
    void formatCardVo(CardTreeChildVo cardVo);

    /**
     * supplySave
     *
     * @param card card
     * @param cardDto cardDto
     */
    void supplySave(Card card, CardDto cardDto);

    /**
     * supplyDelete
     *
     * @param card card
     */
    void supplyDelete(Card card);

}
