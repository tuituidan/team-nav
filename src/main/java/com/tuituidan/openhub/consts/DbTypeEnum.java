package com.tuituidan.openhub.consts;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * DbTypeEnum.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/7/5
 */
@Getter
@AllArgsConstructor
public enum DbTypeEnum {
    /**
     * mysql
     */
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL("postgresql", "org.postgresql.Driver"),
    SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

    private static final Map<String, DbTypeEnum> DATA_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(DbTypeEnum::getType, Function.identity()));

    private String type;

    private String driverClassName;

    /**
     * getByType
     *
     * @param type type
     * @return DbTypeEnum
     */
    public static DbTypeEnum getByType(String type) {
        DbTypeEnum typeEnum = DATA_MAP.get(type);
        Assert.notNull(typeEnum, "不支持的数据库类型-" + type);
        return typeEnum;
    }
}
