package com.tuituidan.openhub;

import com.tuituidan.openhub.util.RandomColorUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * TeamNavApplication.
 *
 * @author tuituidan
 */
@SpringBootApplication
@EnableCaching
public class TeamNavApplication {

    /**
     * 入口方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        //SpringApplication.run(TeamNavApplication.class, args);
        System.out.println(RandomColorUtils.generate());
    }

}
