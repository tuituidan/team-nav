package com.tuituidan.openhub.config;

import com.tuituidan.openhub.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

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

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private UserService userService;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

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
        http.userDetailsService(userService);

        setLogin(http.formLogin());
        setLogout(http.logout());

        http.authorizeRequests()
                .antMatchers(securityProperties.getPermitUrl()).permitAll()
                .antMatchers(securityProperties.getApplyUrl()).authenticated()
                .antMatchers("/api/v1/**").hasAuthority("admin")
                .anyRequest()
                .permitAll();
        http.exceptionHandling().defaultAuthenticationEntryPointFor((request, response, ex) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                request -> "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")));
        return http.build();
    }

    private void setLogin(FormLoginConfigurer<HttpSecurity> login) {
        loginSuccessHandler.setTargetUrlParameter("returnUrl");
        login.loginPage("/login").loginProcessingUrl("/login").successHandler(loginSuccessHandler);
    }

    private void setLogout(LogoutConfigurer<HttpSecurity> logout) {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setUseReferer(true);
        logout.logoutUrl("/logout").logoutSuccessHandler(handler).deleteCookies("JSESSIONID");
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
