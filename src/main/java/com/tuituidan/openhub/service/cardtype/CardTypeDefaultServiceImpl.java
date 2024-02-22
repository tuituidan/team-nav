package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.util.FileExtUtils;
import org.apache.commons.lang3.StringUtils;
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
    public void formatCardVo(CardVo cardVo) {
        // 默认的不用实现
    }

    @Override
    public void supplySave(String id, Card card) {
        // 默认的不用实现
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && !StringUtils.contains(cardIconDto.getSrc(), CardTypeEnum.DEFAULT.getType())) {
            FileExtUtils.deleteFiles(false, cardIconDto.getSrc());
        }
    }

}
