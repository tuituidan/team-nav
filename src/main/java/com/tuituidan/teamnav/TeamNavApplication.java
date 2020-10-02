package com.tuituidan.teamnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * TeamNavApplication.
 *
 * @author zhujunhan
 */
@EnableOpenApi
@SpringBootApplication
public class TeamNavApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamNavApplication.class, args);
    }

}
