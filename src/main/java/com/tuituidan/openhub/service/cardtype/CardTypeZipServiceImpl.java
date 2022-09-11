package com.tuituidan.openhub.service.cardtype;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.bean.vo.CardTreeChildVo;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.service.setting.ISettingListener;
import com.tuituidan.openhub.util.FileExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.ZipUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
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
public class CardTypeZipServiceImpl implements ICardTypeService, ISettingListener {

    private String nginx;

    @Override
    public void formatCardVo(CardTreeChildVo cardVo) {
        if (StringUtils.isNotBlank(nginx)) {
            cardVo.setUrl(nginx + cardVo.getUrl());
        }
    }

    @Override
    public void supplySave(Card card, CardDto cardDto) {
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
            FileExtUtils.deleteFiles(true, existZip.getPath(),
                    "/ext-resources/modules/" + card.getId());
        }
        ZipUtils.unzip(card.getId(), cardDto.getZipDto().getPath());
        card.setZip(JSON.toJSONString(cardDto.getZipDto()));
        card.setUrl(StringExtUtils.format("/ext-resources/modules/{}/index.html", card.getId()));
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = JSON.parseObject(card.getIcon(), CardIconDto.class);
        List<String> deletePaths = new ArrayList<>();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && StringUtils.contains(cardIconDto.getSrc(), "default")) {
            deletePaths.add(cardIconDto.getSrc());
        }
        CardZipDto existZip = JSON.parseObject(card.getZip(), CardZipDto.class);
        deletePaths.add(existZip.getPath());
        deletePaths.add("/ext-resources/modules/" + card.getId());
        FileExtUtils.deleteFiles(false, deletePaths.toArray(new String[0]));
    }

    @Override
    public void settingChange(Setting setting) {
        this.nginx = BooleanUtils.isTrue(setting.getNginxOpen())
                && StringUtils.isNotBlank(setting.getNginxUrl())
                ? setting.getNginxUrl() : StringUtils.EMPTY;
    }

}
