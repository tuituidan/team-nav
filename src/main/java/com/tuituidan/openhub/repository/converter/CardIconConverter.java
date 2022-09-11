package com.tuituidan.openhub.repository.converter;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import java.io.Serializable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * CardIconConverter.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/11
 */
@Converter(autoApply = true)
public class CardIconConverter implements AttributeConverter<CardIconDto, String>, Serializable {

    private static final long serialVersionUID = -6269468044853884404L;

    @Override
    public String convertToDatabaseColumn(CardIconDto attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public CardIconDto convertToEntityAttribute(String dbData) {
        return JSON.parseObject(dbData, CardIconDto.class);
    }

}
