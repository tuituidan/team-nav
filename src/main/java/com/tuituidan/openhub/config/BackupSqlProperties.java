package com.tuituidan.openhub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityProperties.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/23
 */
@Configuration
@ConfigurationProperties(prefix = "backup-sql")
@Getter
@Setter
public class BackupSqlProperties {

    private String selectTable;

    private String selectData;

    private String selectId;

    private String insertSql;
}
