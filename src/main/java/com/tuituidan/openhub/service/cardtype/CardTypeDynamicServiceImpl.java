package com.tuituidan.openhub.service.cardtype;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonFactory;
import com.tuituidan.openhub.annotation.CardType;
import com.tuituidan.openhub.bean.dto.CardDynamicBuilder;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.CardVo;
import com.tuituidan.openhub.consts.AuthTypeEnum;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.util.FileExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.util.stream.Collectors;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * CardTypeDefaultServiceImpl.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Service
@CardType({CardTypeEnum.DYNAMIC_HTTP, CardTypeEnum.DYNAMIC_SQL})
public class CardTypeDynamicServiceImpl implements ICardTypeService {

    @Override
    public void formatCardVo(CardVo cardVo) {
        // 默认的不用实现
    }

    @Override
    public void supplySave(String id, Card card) {
        if (CardTypeEnum.DYNAMIC_HTTP.getType().equals(card.getType())) {
            CardDynamicBuilder dynamicBuilder = card.getDynamicBuilder();
            Assert.isTrue(StringUtils.startsWith(dynamicBuilder.getUrl(), Elements.HTTP),
                    "请求地址请以http://或https://开头");
            if (HttpMethod.POST.matches(dynamicBuilder.getHttpMethod())
                    && JsonFactory.FORMAT_NAME_JSON.equalsIgnoreCase(dynamicBuilder.getBodyType())) {
                Assert.isTrue(JSON.isValid(dynamicBuilder.getBodyJsonData()), "body参数json格式错误");
            }
            if (AuthTypeEnum.KEY_VALUE.getType().equals(dynamicBuilder.getAuthType())) {
                Assert.notEmpty(dynamicBuilder.getAuthKeyValues().stream()
                        .filter(item -> StringUtils.isNoneBlank(item.getKey(), item.getValue()))
                        .collect(Collectors.toList()), "认证键值对不能为空");
            }
            if (AuthTypeEnum.BASIC.getType().equals(dynamicBuilder.getAuthType())) {
                Assert.isTrue(StringUtils.isNoneBlank(dynamicBuilder.getBasicUsername(),
                        dynamicBuilder.getBasicPassword()), "Basic认证的账号密码不能为空");
            }
            if (AuthTypeEnum.BEARER.getType().equals(dynamicBuilder.getAuthType())) {
                Assert.hasText(dynamicBuilder.getBearerToken(), "Bearer Token认证的token不能为空");
            }
            if (AuthTypeEnum.JWT.getType().equals(dynamicBuilder.getAuthType())) {
                Assert.hasText(dynamicBuilder.getJwtSecret(), "jwt认证的密钥不能为空");
            }
        }
        if (CardTypeEnum.DYNAMIC_SQL.getType().equals(card.getType())) {
            CardDynamicBuilder dynamicBuilder = card.getDynamicBuilder();
            Assert.hasText(dynamicBuilder.getSql(), "sql语句不能为空");
            Statement statement = StringExtUtils.getStatement(dynamicBuilder.getSql());
            Assert.isTrue(statement instanceof Select, "sql必须是一个查询语句");
            // todo 判断必须只返回一列
        }
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && !StringUtils.contains(cardIconDto.getSrc(), CardTypeEnum.DEFAULT.getType())) {
            FileExtUtils.deleteFiles(false, cardIconDto.getSrc());
        }
    }

}
