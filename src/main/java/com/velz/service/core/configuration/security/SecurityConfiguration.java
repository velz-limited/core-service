package com.velz.service.core.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO J: Implement authentication.
        http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll()).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
