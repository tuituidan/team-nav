package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.vo.AttachmentVo;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.bean.vo.CategoryVo;
import com.tuituidan.openhub.bean.vo.HomeDataVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.service.cardtype.CardTypeServiceFactory;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.TransactionUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    private CacheService cacheService;

    @Resource
    private CardTypeServiceFactory cardTypeServiceFactory;

    @Resource
    private AttachmentService attachmentService;

    /**
     * 首页查询
     *
     * @param keywords keywords
     * @return List
     */
    public HomeDataVo tree(String keywords) {
        List<CategoryVo> categoryList = getCategoryWithCard(keywords);
        if (CollectionUtils.isEmpty(categoryList)) {
            return new HomeDataVo(Collections.emptyList(), Collections.emptyList());
        }
        List<CategoryVo> rightList = new ArrayList<>();
        Map<String, CategoryVo> lowMap = new HashMap<>();
        for (CategoryVo item : categoryList) {
            if (item.getLevel() <= 2) {
                rightList.add(item);
                continue;
            }
            lowMap.computeIfAbsent(item.getPid(), key -> {
                Category parent = cacheService.getCategory(key);
                CategoryVo convert = BeanExtUtils.convert(parent, CategoryVo::new);
                convert.setFlatSort(StringUtils.leftPad(parent.getSort().toString(), 2, '0'));
                convert.setChildren(new ArrayList<>());
                return convert;
            }).getChildren().add(item);
        }
        rightList.addAll(lowMap.values());
        Collection<CategoryVo> menus = buildMenus(rightList);
        rightList.sort(Comparator.comparing(CategoryVo::getFlatSort));
        return new HomeDataVo(ListUtils.buildTree(menus), rightList);
    }

    private List<CategoryVo> getCategoryWithCard(String keywords) {
        List<CategoryVo> categories = categoryService.getCategoryByLoginUser();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        Map<String, List<CardVo>> cardMap = getCategoryCardMap(keywords);
        if (MapUtils.isEmpty(cardMap)) {
            return Collections.emptyList();
        }
        return setCardsToCategory(categories, cardMap);
    }

    private List<CategoryVo> setCardsToCategory(List<CategoryVo> categories, Map<String, List<CardVo>> cardMap) {
        categories = categories.stream().filter(item -> cardMap.containsKey(item.getId()))
                .collect(Collectors.toList());
        for (CategoryVo item : categories) {
            List<CardVo> cardList = cardMap.get(item.getId());
            cardList.sort(Comparator.comparing(CardVo::getSort));
            item.setCards(cardList);
            item.setCardCount((long) cardList.size());
            item.setFlatSort(StringUtils.leftPad(item.getSort().toString(), 2, '0'));
        }
        return categories;
    }

    private Collection<CategoryVo> buildMenus(List<CategoryVo> highList) {
        Map<String, CategoryVo> menus = new HashMap<>();
        for (CategoryVo item : highList) {
            menus.put(item.getId(), BeanExtUtils.convert(item, CategoryVo::new));
            if ("0".equals(item.getPid())) {
                continue;
            }
            Category parent = cacheService.getCategory(item.getPid());
            item.setFlatSort(StringUtils.leftPad(parent.getSort().toString(), 2, '0')
                    + item.getFlatSort());
            item.setName(parent.getName() + " / " + item.getName());
            menus.computeIfAbsent(item.getPid(), key -> BeanExtUtils.convert(parent, CategoryVo::new));
        }
        return menus.values();
    }

    private Map<String, List<CardVo>> getCategoryCardMap(String keywords) {
        List<Card> cards = StringUtils.isBlank(keywords)
                ? cardRepository.findAll() : cardRepository.findByKeywords(keywords.toLowerCase());
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyMap();
        }
        List<Function<CardVo, String>> tipsFunc = new ArrayList<>();
        tipsFunc.add(CardVo::getTitle);
        tipsFunc.add(CardVo::getContent);
        if (Objects.nonNull(SecurityUtils.getUserInfo())) {
            tipsFunc.add(CardVo::getPrivateContent);
        }
        tipsFunc.add(CardVo::getUrl);
        Map<String, List<AttachmentVo>> attachmentMap = attachmentService.getCardAttachmentMap(cards);
        return cards.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            cardTypeServiceFactory.getService(item.getType()).formatCardVo(vo);
            vo.setTip(tipsFunc.stream().map(func -> func.apply(vo))
                    .filter(StringUtils::isNotBlank).distinct()
                    .collect(Collectors.joining("<br/>")));
            vo.setAttachments(attachmentMap.get(item.getId()));
            return vo;
        }).collect(Collectors.groupingBy(CardVo::getCategory));
    }

    /**
     * 根据分类查
     *
     * @param category category
     * @return List
     */
    public List<CardVo> select(String category) {
        List<Card> list = cardRepository.findByCategory(category);
        Map<String, List<AttachmentVo>> attachmentMap = attachmentService.getCardAttachmentMap(list);
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.buildCategoryName(item.getCategory()));
            vo.setAttachments(attachmentMap.get(item.getId()));
            return vo;
        }).sorted(Comparator.comparing(CardVo::getSort)).collect(Collectors.toList());
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
        TransactionUtils.execute(() -> {
            card.setHasAttachment(ArrayUtils.isNotEmpty(cardDto.getAttachmentIds()));
            cardRepository.save(card);
            attachmentService.saveAttachment(card.getId(), cardDto.getAttachmentIds());
        });
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
     * @param sortDto sortDto
     */
    public void changeSort(String category, SortDto sortDto) {
        List<Card> cards = cardRepository.findByCategory(category).stream()
                .sorted(Comparator.comparing(Card::getSort)).collect(Collectors.toList());
        List<Card> saveList = ListUtils.changeSort(cards, sortDto);
        if (CollectionUtils.isEmpty(saveList)) {
            return;
        }
        cardRepository.saveAll(saveList);
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(String[] id) {
        List<String> ids = Arrays.asList(id);
        List<Card> cards = cardRepository.findAllById(ids);
        TransactionUtils.execute(() -> {
            cardRepository.deleteAllById(ids);
            attachmentService.deleteByBusinessIds(ids);
        });
        for (Card card : cards) {
            cardTypeServiceFactory.getService(card.getType()).supplyDelete(card);
        }
    }

}
