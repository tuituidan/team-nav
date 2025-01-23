package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.service.SettingService;
import com.tuituidan.openhub.util.FileExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.ZipUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private SettingService settingService;

    @Resource
    private CardRepository cardRepository;

    @Override
    public void formatCardVo(CardVo cardVo) {
        Setting settingCache = settingService.getSettingCache();
        if (StringUtils.isNotBlank(settingCache.getNginxUrl())) {
            cardVo.setUrl(settingCache.getNginxUrl() + cardVo.getUrl());
        }
    }

    /**
     * 已存在的卡片修改压缩文件
     *
     * @param id id
     * @param card card
     */
    @Override
    public void supplySave(String id, Card card) {
        if (StringUtils.isNotBlank(id)) {
            String existPath = cardRepository.findById(id).map(Card::getZip)
                    .map(CardZipDto::getPath)
                    .orElse(StringUtils.EMPTY);
            String curPath = Optional.of(card).map(Card::getZip)
                    .map(CardZipDto::getPath)
                    .orElse(StringUtils.EMPTY);
            if (Objects.equals(existPath, curPath)) {
                // 没有上传新的压缩包就不重新解压了
                return;
            }
        }
        FileExtUtils.deleteFiles(true, "/ext-resources/modules/" + card.getId());
        if (null!=card.getZip()){
            ZipUtils.unzip(card.getId(), card.getZip().getPath());
        }
        Optional.ofNullable(card.getZip()).
                ifPresent(zip-> ZipUtils.unzip(card.getId(), zip.getPath()));
        //卡片存放压缩文件的地址是以‘日期_uuid’的格式命名的文件夹
        card.setUrl(StringExtUtils.format("/ext-resources/modules/{}/index.html", card.getId()));
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        List<String> deletePaths = new ArrayList<>();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && !StringUtils.contains(cardIconDto.getSrc(), CardTypeEnum.DEFAULT.getType())) {
            deletePaths.add(cardIconDto.getSrc());
        }
        Optional.ofNullable(card.getZip()).ifPresent(zip->deletePaths.add(zip.getPath())); ;
        deletePaths.add("/ext-resources/modules/" + card.getId());
        FileExtUtils.deleteFiles(false, deletePaths.toArray(new String[0]));
    }

}
