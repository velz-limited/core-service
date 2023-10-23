package com.velz.service.core._base.security;

import com.velz.service.core._base.AppProperties;
import com.velz.service.core._base.security.jwt.JWTAuthenticationFilter;
import com.velz.service.core._base.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.velz.service.core._base.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.velz.service.core._base.security.oauth2.OAuth2AuthorizationRequestRepository;
import com.velz.service.core._base.security.oauth2.OAuth2UserService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private OAuth2AuthorizationRequestRepository oauth2AuthorizationRequestRepository;

    @Autowired
    private OAuth2UserService oauth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(appProperties.getCors().getAllowCredentials());
        configuration.setMaxAge(appProperties.getCors().getMaxAge());
        if (Boolean.TRUE.equals(appProperties.getCors().getAllowAllOrigins())) {
            configuration.setAllowedOriginPatterns(List.of("*"));
        } else {
            configuration.setAllowedOrigins(appProperties.getCors().getAllowOrigins());
        }
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/user/auth/sign-up", "/user/auth/sign-in", "/user/auth/refresh-token").permitAll()
                        .requestMatchers("/user/auth/email-verification", "/user/auth/send-email-verification").permitAll()
                        .requestMatchers("/user/auth/password-reset", "/user/auth/send-password-reset").permitAll()
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
