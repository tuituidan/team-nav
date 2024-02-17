package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.CardVo;

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
    void formatCardVo(CardVo cardVo);

    /**
     * supplySave
     *
     * @param id id
     * @param card card
     */
    void supplySave(String id, Card card);

    /**
     * supplyDelete
     *
     * @param card card
     */
    void supplyDelete(Card card);

}
