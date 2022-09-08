package com.tuituidan.openhub.service.cardtype;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.service.CommonService;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.ZipUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * CardTypeZipService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Service
@CardType(CardTypeEnum.ZIP)
public class CardTypeZipServiceImpl implements ICardTypeService {

    @Resource
    private CommonService commonService;

    @Override
    public void format(Card card, CardDto cardDto) {
        if (StringUtils.isNotBlank(card.getZip())) {
            CardZipDto existZip = JSON.parseObject(card.getZip(), CardZipDto.class);
            if (StringUtils.equals(existZip.getPath(), cardDto.getZipDto().getPath())) {
                if (StringUtils.equals(existZip.getName(), cardDto.getZipDto().getName())) {
                    return;
                }
                existZip.setName(cardDto.getZipDto().getName());
                card.setZip(JSON.toJSONString(existZip));
                return;
            }
            List<String> deletePaths = new ArrayList<>();
            deletePaths.add(existZip.getPath());
            deletePaths.add(StringExtUtils.format("/ext-resources/modules/{}", card.getId()));
            commonService.deleteFiles(true, deletePaths);
        }
        ZipUtils.unzip(card.getId(), cardDto.getZipDto().getPath());
        card.setZip(JSON.toJSONString(cardDto.getZipDto()));
        card.setUrl(StringExtUtils.format("/ext-resources/modules/{}/index.html", card.getId()));
    }

}
