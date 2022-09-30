package com.tuituidan.openhub.config;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${login.enable}")
    private boolean loginEnable;

    /**
     * filterChain
     *
     * @param http http
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (!loginEnable) {
            return http.authorizeRequests().anyRequest().permitAll()
                    .and().headers().frameOptions().disable()
                    .and().csrf().disable().build();
        }
        return http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/api/v1/login")
                .and().logout()
                .logoutUrl("/logout").logoutSuccessUrl("/admin/category")
                .deleteCookies("JSESSIONID")
                .and().authorizeRequests()
                .antMatchers("/api/v1/card/tree", "/api/v1/qrcode").permitAll()
                .and().authorizeRequests()
                .antMatchers("/admin/**", "/api/v1/**").authenticated()
                .anyRequest().permitAll()
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor((request, response, ex) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                        request -> "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")))
                .and().build();
    }

}
