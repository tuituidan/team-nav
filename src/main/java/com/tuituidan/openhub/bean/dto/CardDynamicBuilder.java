package com.tuituidan.openhub.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CardDynamic.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/16
 */
@Getter
@Setter
@Accessors(chain = true)
public class CardDynamicBuilder implements Serializable {

    private static final long serialVersionUID = -1275372703627864650L;

    private String datasourceId;

    private String sql;

    private String url;

    private String httpMethod;

    private List<KeyValueDto> queryParams;

    private String bodyType;

    private List<KeyValueDto> bodyFormData;

    private String bodyJsonData;

    private String authType;

    private List<KeyValueDto> authKeyValues;

    private String basicUsername;

    private String basicPassword;

    private String bearerToken;

    private String jwtAlgorithm;

    private String jwtSecret;

    private List<KeyValueDto> jwtPayload;

    private String resultExp;

}
