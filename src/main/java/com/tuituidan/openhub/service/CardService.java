package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.vo.CardTreeChildVo;
import com.tuituidan.openhub.bean.vo.CardTreeVo;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.service.cardtype.CardTypeServiceFactory;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
                ? cardRepository.findAll() : cardRepository.findByKeywords(keywords.toLowerCase());
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyList();
        }
        Map<String, List<Card>> cardMap = cards.stream().collect(Collectors.groupingBy(Card::getCategory));
        return categories.stream()
                .map(category -> BeanExtUtils.convert(category, CardTreeVo::new)
                        .setChildren(setCardChildren(cardMap.get(category.getId()))))
                .filter(item -> CollectionUtils.isNotEmpty(item.getChildren()))
                .sorted(Comparator.comparing(CardTreeVo::getSort)).collect(Collectors.toList());
    }

    private List<CardTreeChildVo> setCardChildren(List<Card> cardList) {
        if (CollectionUtils.isEmpty(cardList)) {
            return Collections.emptyList();
        }
        List<CardTreeChildVo> result = new ArrayList<>();
        for (Card card : cardList) {
            CardTreeChildVo vo = BeanExtUtils.convert(card, CardTreeChildVo::new);
            cardTypeServiceFactory.getService(card.getType()).formatCardVo(vo);
            vo.setTip(Stream.of(vo.getTitle(), vo.getContent(), vo.getUrl())
                    .filter(StringUtils::isNotBlank).distinct()
                    .collect(Collectors.joining("\n")));
            result.add(vo);
        }
        result.sort(Comparator.comparing(CardTreeChildVo::getSort));
        return result;
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
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 保存卡片
     *
     * @param id id
     * @param cardDto cardDto
     */
    public void save(String id, CardDto cardDto) {
        this.saveIcon(cardDto.getIcon());
        Card card = BeanExtUtils.convert(cardDto, Card::new);
        card.setId(StringUtils.isBlank(id) ? StringExtUtils.getUuid() : id);
        cardTypeServiceFactory.getService(card.getType()).supplySave(id, card);
        if (card.getSort() == null) {
            card.setSort(cardRepository.getMaxSort(card.getCategory()) + 1);
        }
        cardRepository.save(card);
    }

    private void saveIcon(CardIconDto cardIconDto) {
        if (!StringUtils.startsWith(cardIconDto.getSrc(), "http")) {
            return;
        }
        String path = StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), FilenameUtils.getExtension(cardIconDto.getSrc()));
        File saveFile = new File(Consts.ROOT_DIR + path);
        try {
            FileUtils.forceMkdirParent(saveFile);
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
        try (OutputStream outputStream = new FileOutputStream(saveFile)) {
            IOUtils.copy(new URL(cardIconDto.getSrc()), outputStream);
            cardIconDto.setSrc(path);
        } catch (Exception ex) {
            log.error("card icon 保存失败", ex);
        }
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
        Card card = cardRepository.findById(id).orElseThrow(NullPointerException::new);
        cardRepository.deleteById(id);
        cardTypeServiceFactory.getService(card.getType()).supplyDelete(card);
    }

}
