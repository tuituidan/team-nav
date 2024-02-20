package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.bean.vo.CategoryVo;
import com.tuituidan.openhub.bean.vo.HomeDataVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.repository.CategoryRepository;
import com.tuituidan.openhub.service.cardtype.CardTypeServiceFactory;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.StringExtUtils;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
    private CacheService cacheService;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private CardTypeServiceFactory cardTypeServiceFactory;

    @Resource
    private CommonService commonService;

    /**
     * 首页查询
     *
     * @param keywords keywords
     * @return List
     */
    public HomeDataVo tree(String keywords) {
        List<Category> categories = categoryRepository.findByValidTrueOrderBySort();
        if (CollectionUtils.isEmpty(categories)) {
            return new HomeDataVo(Collections.emptyList(), Collections.emptyList());
        }
        Map<String, List<CardVo>> cardMap = getCardMap(keywords);
        if (MapUtils.isEmpty(cardMap)) {
            return new HomeDataVo(Collections.emptyList(), Collections.emptyList());
        }
        List<CategoryVo> categoryList = collectCards(categories, cardMap);
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

    private List<CategoryVo> collectCards(List<Category> categories, Map<String, List<CardVo>> cardMap) {
        return categories.stream().filter(item -> cardMap.containsKey(item.getId()))
                .map(item -> {
                    CategoryVo vo = BeanExtUtils.convert(item, CategoryVo::new);
                    List<CardVo> cardList = cardMap.get(vo.getId());
                    cardList.sort(Comparator.comparing(CardVo::getSort));
                    vo.setCards(cardList);
                    vo.setCardCount((long) cardList.size());
                    vo.setFlatSort(StringUtils.leftPad(item.getSort().toString(), 2, '0'));
                    return vo;
                }).collect(Collectors.toList());
    }

    private Collection<CategoryVo> buildMenus(List<CategoryVo> highList) {
        Map<String, CategoryVo> menus = new HashMap<>();
        for (CategoryVo item : highList) {
            menus.put(item.getId(), BeanExtUtils.convert(item, CategoryVo::new));
            if ("0".equals(item.getPid())) {
                continue;
            }
            Category parent = cacheService.getCategory(item.getPid());
            item.setFlatSort(StringUtils.leftPad(parent.getSort().toString(), 2, '0') + item.getFlatSort());
            item.setName(parent.getName() + " / " + item.getName());
            menus.computeIfAbsent(item.getPid(), key -> BeanExtUtils.convert(parent, CategoryVo::new));
        }
        return menus.values();
    }

    private Map<String, List<CardVo>> getCardMap(String keywords) {
        List<Card> cards = StringUtils.isBlank(keywords)
                ? cardRepository.findAll() : cardRepository.findByKeywords(keywords.toLowerCase());
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyMap();
        }
        return cards.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            cardTypeServiceFactory.getService(item.getType()).formatCardVo(vo);
            vo.setTip(Stream.of(vo.getTitle(), vo.getContent(), vo.getUrl())
                    .filter(StringUtils::isNotBlank).distinct()
                    .collect(Collectors.joining("<br/>")));
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
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.buildCategoryName(item.getCategory()));
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
     * @param sortDto sortDto
     */
    public void changeSort(String category, SortDto sortDto) {
        Supplier<List<Card>> supplier = () -> cardRepository.findByCategory(category).stream()
                .sorted(Comparator.comparing(Card::getSort)).collect(Collectors.toList());
        ListUtils.changeSort(supplier,
                cardRepository::saveAll,
                sortDto);
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(String[] id) {
        List<String> ids = Arrays.asList(id);
        List<Card> cards = cardRepository.findAllById(ids);
        cardRepository.deleteAllById(ids);
        for (Card card : cards) {
            cardTypeServiceFactory.getService(card.getType()).supplyDelete(card);
        }
    }

}
