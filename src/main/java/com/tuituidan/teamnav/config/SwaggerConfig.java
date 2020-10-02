package com.tuituidan.teamnav.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tuituidan.teamnav.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("团队网址导航")
                .description("团队网址导航。")
                .termsOfServiceUrl("https://github.com/tuituidan/team-nav")
                .contact(new Contact("zhujunhan", "http://www.tuituidan.com", "tuituidan@163.com"))
                .version("1.0")
                .build();
    }
}
