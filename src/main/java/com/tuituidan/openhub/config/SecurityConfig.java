package com.tuituidan.openhub.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * filterChain
     *
     * @param http http
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/api/v1/login")
                .and().logout()
                .logoutUrl("/logout").logoutSuccessUrl("/admin/category")
                .deleteCookies("JSESSIONID")
                .and().authorizeRequests()
                .antMatchers("/api/v1/card/tree").permitAll()
                .and().authorizeRequests()
                .antMatchers("/admin/**", "/api/v1/**").authenticated()
                .anyRequest().permitAll()
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
                .build();
    }

}
