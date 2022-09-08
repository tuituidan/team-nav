package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.consts.CardTypeEnum;
import org.springframework.stereotype.Service;

/**
 * CardTypeDefaultServiceImpl.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Service
@CardType(CardTypeEnum.DEFAULT)
public class CardTypeDefaultServiceImpl implements ICardTypeService {

    @Override
    public void format(Card card, CardDto cardDto) {
        // 默认的不用实现
    }

}
