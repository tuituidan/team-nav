package com.tuituidan.openhub.service.cardtype;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.CardTreeChildVo;
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
    public void formatCardVo(CardTreeChildVo cardVo) {
        // 无特殊处理
    }

    @Override
    public void supplySave(Card card, CardDto cardDto) {
        // 默认的不用实现
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = JSON.parseObject(card.getIcon(), CardIconDto.class);
        if (StringUtils.isNotBlank(cardIconDto.getSrc())) {
            FileExtUtils.deleteFiles(false, cardIconDto.getSrc());
        }
    }

}
