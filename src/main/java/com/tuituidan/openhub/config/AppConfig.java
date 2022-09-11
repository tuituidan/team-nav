package com.tuituidan.openhub.config;

import com.tuituidan.openhub.consts.Consts;
import java.io.File;
import javax.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * AppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/5
 */
@Configuration
public class AppConfig {

    /**
     * 明确指定上传文件的临时目录，避免Tomcat使用默认的tmp而被操作系统清理掉
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = Consts.ROOT_DIR + "/temp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            Assert.isTrue(tmpFile.mkdirs(), "临时上传路径创建失败");
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }

}
