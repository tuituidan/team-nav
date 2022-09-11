package com.tuituidan.openhub.repository.converter;

import com.alibaba.fastjson.JSON;
import com.tuituidan.openhub.bean.dto.CardZipDto;
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
public class CardZipConverter implements AttributeConverter<CardZipDto, String>, Serializable {

    private static final long serialVersionUID = 6353275767541866237L;

    @Override
    public String convertToDatabaseColumn(CardZipDto attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public CardZipDto convertToEntityAttribute(String dbData) {
        return JSON.parseObject(dbData, CardZipDto.class);
    }

}
