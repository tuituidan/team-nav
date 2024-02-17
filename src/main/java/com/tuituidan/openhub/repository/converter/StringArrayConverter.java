package com.tuituidan.openhub.repository.converter;

import java.io.Serializable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * CardIconConverter.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/11
 */
@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<String[], String>, Serializable {

    private static final long serialVersionUID = -5565439586824397699L;

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        if (ArrayUtils.isEmpty(attribute)) {
            return null;
        }
        return "," + StringUtils.join(attribute, ",") + ",";
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return StringUtils.strip(dbData, ",").split(",");
    }

}
