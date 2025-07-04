package com.tuituidan.openhub.repository.converter;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.bean.dto.CardDynamicBuilder;
import java.io.Serializable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * CardZipConverter.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/11
 */
@Converter(autoApply = true)
public class CardDynamicBuilderConverter implements AttributeConverter<CardDynamicBuilder, String>, Serializable {

    private static final long serialVersionUID = 3396194682017154771L;

    @Override
    public String convertToDatabaseColumn(CardDynamicBuilder attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public CardDynamicBuilder convertToEntityAttribute(String dbData) {
        return JSON.parseObject(dbData, CardDynamicBuilder.class);
    }

}
