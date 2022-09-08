package com.tuituidan.openhub.service;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.bean.vo.CardTreeChildVo;
import com.tuituidan.openhub.bean.vo.CardTreeVo;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.service.cardtype.CardTypeServiceFactory;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * CardService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
@Slf4j
public class CardService {

    @Resource
    private CardRepository cardRepository;

    @Resource
    private CategoryService categoryService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CommonService commonService;

    @Resource
    private SettingService settingService;

    @Resource
    private CardTypeServiceFactory cardTypeServiceFactory;

    /**
     * 首页查询
     *
     * @param keywords keywords
     * @return List
     */
    public List<CardTreeVo> tree(String keywords) {
        List<Category> categories = categoryService.select();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        List<Card> cards = StringUtils.isBlank(keywords)
                ? cardRepository.findAll() : cardRepository.findByTitleContainsOrContentContains(keywords, keywords);
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyList();
        }
        Setting setting = settingService.get();
        String nginx = BooleanUtils.isTrue(setting.getNginxOpen())
                && StringUtils.isNotBlank(setting.getNginxUrl()) ? setting.getNginxUrl() : StringUtils.EMPTY;
        Map<String, List<Card>> cardMap = cards.stream().collect(Collectors.groupingBy(Card::getCategory));
        return categories.stream()
                .map(category -> BeanExtUtils.convert(category, CardTreeVo::new)
                        .setChildren(setCardChildren(cardMap.get(category.getId()), nginx)))
                .filter(item -> CollectionUtils.isNotEmpty(item.getChildren()))
                .sorted(Comparator.comparing(CardTreeVo::getSort)).collect(Collectors.toList());
    }

    private List<CardTreeChildVo> setCardChildren(List<Card> cardList, String nginx) {
        if (CollectionUtils.isEmpty(cardList)) {
            return Collections.emptyList();
        }
        return cardList.stream()
                .map(card -> {
                    CardTreeChildVo vo = BeanExtUtils.convert(card, CardTreeChildVo::new);
                    vo.setIcon(JSON.parseObject(card.getIcon(), CardIconDto.class));
                    if (Consts.CARD_TYPE_ZIP.equals(card.getType()) && StringUtils.isNotBlank(nginx)) {
                        vo.setUrl(nginx + vo.getUrl());
                    }
                    vo.setTip(Stream.of(vo.getTitle(), vo.getContent(), vo.getUrl())
                            .filter(StringUtils::isNotBlank).distinct()
                            .collect(Collectors.joining("\n")));
                    return vo;
                })
                .sorted(Comparator.comparing(CardTreeChildVo::getSort))
                .collect(Collectors.toList());
    }

    /**
     * 根据分类查
     *
     * @param category category
     * @return List
     */
    public List<CardVo> select(String category) {
        List<Card> list = cardRepository.findByCategoryOrderBySortAsc(category);
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.get(item.getCategory()).getName());
            vo.setIconDto(JSON.parseObject(item.getIcon(), CardIconDto.class));
            if (StringUtils.isNotBlank(item.getZip())) {
                vo.setZipDto(JSON.parseObject(item.getZip(), CardZipDto.class));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 保存卡片
     *
     * @param cardDto cardDto
     */
    public void save(CardDto cardDto) {
        Card card = BeanExtUtils.convert(cardDto, Card::new);
        card.setIcon(saveIcon(cardDto.getIconDto()));
        if (StringUtils.isBlank(card.getId())) {
            card.setId(StringExtUtils.getUuid());
        }
        cardTypeServiceFactory.format(card, cardDto);
        if (card.getSort() == null) {
            card.setSort(cardRepository.getMaxSort(card.getCategory()) + 1);
        }
        cardRepository.save(card);
    }

    private String saveIcon(CardIconDto cardIconDto) {
        if (StringUtils.isBlank(cardIconDto.getSrc())
                || StringUtils.startsWith(cardIconDto.getSrc(), "/ext-resources")) {
            return JSON.toJSONString(cardIconDto);
        }
        try {
            ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(cardIconDto.getSrc(), byte[].class);
            String url = StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                    DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                    StringExtUtils.getUuid(), FilenameUtils.getExtension(cardIconDto.getSrc()));
            Assert.notNull(forEntity.getBody(), "获取数据失败");
            FileUtils.writeByteArrayToFile(new File(Consts.ROOT_DIR + url),
                    forEntity.getBody());
            cardIconDto.setSrc(url);
        } catch (Exception ex) {
            log.error("card icon 保存失败", ex);
        }
        return JSON.toJSONString(cardIconDto);
    }

    /**
     * 上移和下移，交换两个卡片序号
     *
     * @param category 分类ID
     * @param before 原来的索引
     * @param after 调整后的索引
     */
    public void changeSort(String category, int before, int after) {
        if (before == after) {
            return;
        }
        LinkedList<Card> list = new LinkedList<>(cardRepository.findByCategoryOrderBySortAsc(category));
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        list.add(after, list.remove(before));
        List<Card> updateList = new ArrayList<>();
        int index = 0;
        for (Card item : list) {
            if (!Objects.equals(item.getSort(), index)) {
                updateList.add(item.setSort(index));
            }
            index++;
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            cardRepository.saveAll(updateList);
        }
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(String id) {
        Card card = cardRepository.findById(id).orElse(new Card());
        CardIconDto cardIconDto = JSON.parseObject(card.getIcon(), CardIconDto.class);
        cardRepository.deleteById(id);
        List<String> deletePaths = new ArrayList<>();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())) {
            deletePaths.add(cardIconDto.getSrc());
        }
        if (Consts.CARD_TYPE_ZIP.equals(card.getType())) {
            CardZipDto existZip = JSON.parseObject(card.getZip(), CardZipDto.class);
            deletePaths.add(existZip.getPath());
            deletePaths.add(StringExtUtils.format("/ext-resources/modules/{}", card.getId()));
        }
        commonService.deleteFiles(false, deletePaths);
    }

}
