package com.tuituidan.teamnav;

import com.tuituidan.teamnav.util.IpKit;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * TeamNavApplication.
 *
 * @author zhujunhan
 */
@EnableOpenApi
@SpringBootApplication
@Slf4j
public class TeamNavApplication implements CommandLineRunner {

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(TeamNavApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String port = environment.getProperty("local.server.port");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        log.info("http://{}:{}{}", IpKit.getIpAddress(), port, contextPath);
    }
}
