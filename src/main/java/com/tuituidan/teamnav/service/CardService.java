package com.tuituidan.teamnav.service;

import com.alibaba.fastjson.JSON;
import com.tuituidan.teamnav.bean.dto.CardDto;
import com.tuituidan.teamnav.bean.dto.CardIconDto;
import com.tuituidan.teamnav.bean.dto.CardZipDto;
import com.tuituidan.teamnav.bean.entity.Card;
import com.tuituidan.teamnav.bean.entity.Category;
import com.tuituidan.teamnav.bean.vo.CardTreeChildVo;
import com.tuituidan.teamnav.bean.vo.CardTreeVo;
import com.tuituidan.teamnav.bean.vo.CardVo;
import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.repository.CardRepository;
import com.tuituidan.teamnav.util.BeanExtUtils;
import com.tuituidan.teamnav.util.CompletableUtils;
import com.tuituidan.teamnav.util.StringExtUtils;
import com.tuituidan.teamnav.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * CardService.
 *
 * @author zhujunhan
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

    public List<CardTreeVo> tree() {
        List<Category> categories = categoryService.select();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        List<Card> cards = cardRepository.findAll();
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyList();
        }
        Map<String, List<Card>> cardMap = cards.stream().collect(Collectors.groupingBy(Card::getCategory));
        return categories.stream()
                .map(category -> {
                    CardTreeVo cardTreeVo = BeanExtUtils.convert(category, CardTreeVo.class);
                    List<Card> cardList = cardMap.get(category.getId());
                    if (CollectionUtils.isNotEmpty(cardList)) {
                        cardTreeVo.setChildren(cardList.stream()
                                .map(card -> {
                                    CardTreeChildVo cardTreeChildVo = BeanExtUtils.convert(card, CardTreeChildVo.class);
                                    cardTreeChildVo.setIcon(JSON.parseObject(card.getIcon(), CardIconDto.class));
                                    return cardTreeChildVo;
                                })
                                .sorted(Comparator.comparing(CardTreeChildVo::getSort))
                                .collect(Collectors.toList()));
                    }
                    return cardTreeVo;
                })
                .filter(item -> Objects.nonNull(item.getChildren()))
                .sorted(Comparator.comparing(CardTreeVo::getSort)).collect(Collectors.toList());
    }

    public List<CardVo> select(String category) {
        List<Card> list = cardRepository.findByCategoryOrderBySortAsc(category);
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo.class);
            vo.setCategoryName(categoryService.get(item.getCategory()).getName());
            vo.setIconDto(JSON.parseObject(item.getIcon(), CardIconDto.class));
            if (StringUtils.isNotBlank(item.getZip())) {
                vo.setZipDto(JSON.parseObject(item.getZip(), CardZipDto.class));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    public void save(CardDto cardDto) {
        Card card = BeanExtUtils.convert(cardDto, Card.class);
        card.setIcon(saveIcon(cardDto.getIconDto()));
        if (Consts.CARD_TYPE_ZIP.equals(cardDto.getType())) {
            saveZip(card, cardDto);
        }
        if (StringUtils.isBlank(cardDto.getId())) {
            Integer maxSort = cardRepository.findAll().stream().max(Comparator.comparing(Card::getSort))
                    .map(Card::getSort).orElse(0);
            card.setSort(maxSort + 1);
        }
        cardRepository.save(card);
    }

    private void saveZip(Card card, CardDto cardDto) {
        if (StringUtils.isNotBlank(cardDto.getZip())) {
            CardZipDto existZip = JSON.parseObject(cardDto.getZip(), CardZipDto.class);
            if (StringUtils.equals(existZip.getPath(), cardDto.getZipDto().getPath())) {
                if (StringUtils.equals(existZip.getName(), cardDto.getZipDto().getName())) {
                    return;
                }
                existZip.setName(cardDto.getZipDto().getName());
                card.setZip(JSON.toJSONString(existZip));
                return;
            }
            asyncDeleteOld(existZip.getPath());
        }
        card.setZip(JSON.toJSONString(cardDto.getZipDto()));
        card.setUrl(ZipUtils.unzip(cardDto.getZipDto()));
    }

    private String saveIcon(CardIconDto cardIconDto) {
        if (StringUtils.isBlank(cardIconDto.getSrc())
                || StringUtils.startsWith(cardIconDto.getSrc(), "/team-nav-images")) {
            return JSON.toJSONString(cardIconDto);
        }
        try {
            ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(cardIconDto.getSrc(), byte[].class);
            String url = StringExtUtils.format("/team-nav-images/{}/{}.{}",
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

    public void changeSort(String id, String direction) {
        List<Card> list = cardRepository.findAll(Sort.by(Sort.Direction.fromString(direction), "sort"));
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        int index = 0;
        for (Card item : list) {
            if (item.getId().equals(id)) {
                Card last = list.get(index - 1);
                Integer lastSort = last.getSort();
                last.setSort(item.getSort());
                item.setSort(lastSort);
                cardRepository.saveAll(Arrays.asList(last, item));
                break;
            }
            index++;
        }
    }

    public void delete(String id) {
        Card card = cardRepository.findById(id).orElse(new Card());
        CardIconDto cardIconDto = JSON.parseObject(card.getIcon(), CardIconDto.class);
        cardRepository.deleteById(id);
        if (StringUtils.isNotBlank(cardIconDto.getSrc())) {
            asyncDeleteOld(cardIconDto.getSrc());
        }
        if (Consts.CARD_TYPE_ZIP.equals(card.getType())) {
            CardZipDto existZip = JSON.parseObject(card.getZip(), CardZipDto.class);
            asyncDeleteOld(existZip.getPath());
        }
    }

    private void asyncDeleteOld(String path) {
        CompletableUtils.execute(() -> {
            try {
                FileUtils.forceDelete(new File(Consts.ROOT_DIR + path));
                if (StringUtils.endsWith(path, ".zip")) {
                    FileUtils.forceDelete(new File(Consts.ROOT_DIR
                            + StringUtils.substringBeforeLast(path, ".")));
                }
            } catch (IOException ex) {
                log.error("删除原文件失败：【{}】", path, ex);
            }
        });
    }

}
