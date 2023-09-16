package com.velz.service.core.configuration.security;

import com.velz.service.core.configuration.security.jwt.JWTAuthenticationFilter;
import com.velz.service.core.configuration.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.velz.service.core.configuration.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.velz.service.core.configuration.security.oauth2.OAuth2AuthorizationRequestRepository;
import com.velz.service.core.configuration.security.oauth2.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private OAuth2AuthorizationRequestRepository oauth2AuthorizationRequestRepository;

    @Autowired
    private OAuth2UserService oauth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/user/sign-up", "/user/sign-in", "/user/refresh-token").permitAll()
                        .requestMatchers("/user/email-verification", "/user/send-email-verification").permitAll()
                        .requestMatchers("/user/password-reset", "/user/send-password-reset").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JWTAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .oauth2Login(c -> c
                        .authorizationEndpoint(sc -> sc.authorizationRequestRepository(oauth2AuthorizationRequestRepository))
                        .userInfoEndpoint(sc -> sc.userService(oauth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler)
                )
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                );
        return http.build();
    }
}
