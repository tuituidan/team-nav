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
@ConfigurationProperties(prefix = "spring.security")
@Getter
@Setter
public class SecurityProperties {

    private String[] permitUrl;

    private String[] generalUserUrl;
}
