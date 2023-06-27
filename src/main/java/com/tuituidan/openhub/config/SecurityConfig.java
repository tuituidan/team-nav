package com.tuituidan.openhub.config;

import com.tuituidan.openhub.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Value("${spring.security.permit-url:}")
    private String[] permitUrl;

    @Resource
    private UserService userService;

    /**
     * filterChain
     *
     * @param http http
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();
        if (!loginEnable) {
            return http.authorizeRequests().anyRequest().permitAll().and().build();
        }
        http.userDetailsService(userService);
        http.formLogin().loginPage("/login").loginProcessingUrl("/api/v1/login");
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/admin/category").deleteCookies("JSESSIONID");
        http.authorizeRequests().antMatchers(permitUrl).permitAll();
        http.authorizeRequests().antMatchers("/admin/**", "/api/v1/**")
                .authenticated().anyRequest().permitAll();
        http.exceptionHandling().defaultAuthenticationEntryPointFor((request, response, ex) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                request -> "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")));
        return http.build();
    }

    /**
     * bCryptPasswordEncoder
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder cryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authenticationManager
     *
     * @param authenticationConfiguration authenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
