package com.tuituidan.openhub.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.tuituidan.openhub.bean.dto.CardDto;
import com.tuituidan.openhub.bean.dto.CardDynamicBuilder;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.entity.Category;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.vo.AttachmentVo;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.bean.vo.CategoryVo;
import com.tuituidan.openhub.bean.vo.HomeDataVo;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.repository.UserRepository;
import com.tuituidan.openhub.service.cardtype.CardTypeServiceFactory;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.HttpUtils;
import com.tuituidan.openhub.util.IconUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.SecurityUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.TransactionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private CacheService cacheService;

    @Resource
    private CardTypeServiceFactory cardTypeServiceFactory;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RestTemplate restTemplate;

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
        for (CategoryVo item : lowMap.values()) {
            item.getChildren().sort(Comparator.comparing(CategoryVo::getFlatSort));
            rightList.add(item);
        }
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
        List<CategoryVo> result = new ArrayList<>();
        for (CategoryVo item : categories) {
            List<CardVo> cardList = cardMap.get(item.getId());
            if (CollectionUtils.isEmpty(cardList)) {
                continue;
            }
            cardList.sort(Comparator.comparing(CardVo::getSort));
            item.setCards(cardList);
            item.setCardCount((long) cardList.size());
            item.setFlatSort(StringUtils.leftPad(item.getSort().toString(), 2, '0'));
            result.add(item);
        }
        return result;
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
                ? cardRepository.findByAuditTrue() : cardRepository.findByKeywords(keywords.toLowerCase());
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyMap();
        }
        String ignoreProperty = SecurityUtils.isLogin() ? null : "privateContent";
        Map<String, List<AttachmentVo>> attachmentMap = attachmentService.getCardAttachmentMap(cards);
        return cards.stream().map(item -> {
            // 敏感信息不暴露
            item.setDynamicBuilder(null);
            CardVo vo = BeanExtUtils.convert(item, CardVo::new, ignoreProperty);
            cardTypeServiceFactory.getService(item.getType()).formatCardVo(vo);
            vo.setAttachments(attachmentMap.get(item.getId()));
            vo.setContent(StringUtils.defaultString(vo.getContent()));
            vo.setStar(false);
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
        List<Card> list = cardRepository.findByAuditTrueAndCategory(category);
        Map<String, List<AttachmentVo>> attachmentMap = attachmentService.getCardAttachmentMap(list);
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.buildCategoryName(item.getCategory()));
            vo.setAttachments(attachmentMap.get(item.getId()));
            return vo;
        }).sorted(Comparator.comparing(CardVo::getSort)).collect(Collectors.toList());
    }

    /**
     * countApply
     *
     * @return long
     */
    public Long countApply() {
        return cardRepository.countByAuditFalse();
    }

    /**
     * 查询申请列表
     *
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<CardVo> selectApply(Integer pageIndex, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("applyTime").descending());
        Page<Card> page = cardRepository.findByAuditFalse(pageRequest);
        if (page.getTotalElements() <= 0) {
            return page.map(item -> new CardVo());
        }
        Map<String, List<AttachmentVo>> attachmentMap = attachmentService.getCardAttachmentMap(page.toList());
        Map<String, String> userMap =
                userRepository.findAllById(page.stream().map(Card::getApplyBy).collect(Collectors.toSet()))
                        .stream().collect(Collectors.toMap(User::getId, User::getNickname));
        return page.map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.buildCategoryName(item.getCategory()));
            vo.setAttachments(attachmentMap.get(item.getId()));
            vo.setApplyBy(userMap.get(vo.getApplyBy()));
            return vo;
        });
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
        if (SecurityUtils.isAdmin(SecurityUtils.getUserInfo())) {
            card.setAudit(true);
        } else {
            card.setAudit(false);
            card.setApplyBy(SecurityUtils.getId());
            card.setApplyTime(LocalDateTime.now());
        }
        TransactionUtils.execute(() -> {
            card.setHasAttachment(ArrayUtils.isNotEmpty(cardDto.getAttachmentIds()));
            cardRepository.save(card);
            attachmentService.saveAttachment(card.getId(), cardDto.getAttachmentIds());
        });
    }

    private void saveIcon(CardIconDto cardIconDto) {
        if (!HttpUtils.isHttp(cardIconDto.getSrc())) {
            return;
        }
        try {
            cardIconDto.setSrc(IconUtils.saveIcon(cardIconDto.getSrc()));
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
        List<Card> cards = cardRepository.findByAuditTrueAndCategory(category).stream()
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

    /**
     * 通过申请
     *
     * @param id id
     */
    public void passApply(String[] id) {
        List<String> ids = Arrays.asList(id);
        List<Card> cards = cardRepository.findAllById(ids);
        cards.forEach(item -> item.setAudit(true));
        cardRepository.saveAll(cards);
    }

    /**
     * 获取动态卡片内容
     *
     * @param id id
     * @return string
     */
    public String cardDynamicContent(String id) {
        Card card = cardRepository.findById(id).orElseThrow(NullPointerException::new);
        if (CardTypeEnum.DYNAMIC_HTTP.getType().equals(card.getType())) {
            CardDynamicBuilder builder = card.getDynamicBuilder();
            Assert.notNull(builder, "数据异常，动态内容构建为空");
            HttpMethod httpMethod = HttpMethod.resolve(builder.getHttpMethod());
            Assert.notNull(httpMethod, "http-method异常");
            String body;
            try {
                ResponseEntity<String> exchange = restTemplate.exchange(
                        HttpUtils.buildQueryParams(builder.getUrl(), builder.getQueryParams()),
                        httpMethod, HttpUtils.buildHttpEntity(builder), String.class);
                Assert.hasText(exchange.getBody(), "接口未返回数据");
                body = exchange.getBody();
            } catch (Exception ex) {
                log.error("http接口请求失败", ex);
                return ex.getMessage();
            }
            if (JSON.isValid(body) && StringUtils.isNotBlank(builder.getResultExp())) {
                return (String) JSONPath.eval(body, builder.getResultExp());
            }
            return body;
        }
        if (CardTypeEnum.DYNAMIC_SQL.getType().equals(card.getType())) {
            CardDynamicBuilder builder = card.getDynamicBuilder();
            Assert.notNull(builder, "数据异常，动态内容构建为空");
            try {
                JdbcTemplate jdbcTemplate = cacheService.getJdbcTemplate(builder.getDatasourceId());
                List<String> results = jdbcTemplate.queryForList(builder.getSql(), String.class);
                if (CollectionUtils.isEmpty(results)) {
                    return StringUtils.EMPTY;
                }
                return results.get(0);
            } catch (Exception ex) {
                log.error("数据库查询失败", ex);
                return ex.getMessage();
            }
        }
        return StringUtils.EMPTY;
    }

}
