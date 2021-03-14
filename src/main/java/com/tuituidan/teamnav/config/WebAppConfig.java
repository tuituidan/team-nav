package com.tuituidan.teamnav.config;

import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.util.StringExtUtils;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebAppConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/9/23
 */
@Component
public class WebAppConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    @Value(value = "#{'${resource-exts}'.split(',')}")
    private String[] resourceExts;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);//.addPathPatterns("/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations 中的参数，必须以【/】结尾!!!
        for (String path : resourceExts) {
            registry.addResourceHandler(StringExtUtils.format("/{}/**", path))
                    .addResourceLocations(StringExtUtils.format("file:{}/{}/",
                            Consts.ROOT_DIR, path));
        }

    }
}
