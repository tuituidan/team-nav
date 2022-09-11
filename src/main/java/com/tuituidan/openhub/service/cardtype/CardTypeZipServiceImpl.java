package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardIconDto;
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
    public void supplySave(Card card) {
        FileExtUtils.deleteFiles(true, "/ext-resources/modules/" + card.getId());
        ZipUtils.unzip(card.getId(), card.getZip().getPath());
        card.setUrl(StringExtUtils.format("/ext-resources/modules/{}/index.html", card.getId()));
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        List<String> deletePaths = new ArrayList<>();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && StringUtils.contains(cardIconDto.getSrc(), "default")) {
            deletePaths.add(cardIconDto.getSrc());
        }
        deletePaths.add(card.getZip().getPath());
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
