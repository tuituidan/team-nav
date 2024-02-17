package com.tuituidan.openhub.config;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.util.StringExtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebAppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/9/23
 */
@Component
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations 中的参数，必须以【/】结尾!!!
        registry.addResourceHandler("/ext-resources/**")
                .addResourceLocations(StringExtUtils.format("file:{}/ext-resources/",
                        Consts.ROOT_DIR));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/docs").setViewName("forward:/docs/index.html");
    }

}
