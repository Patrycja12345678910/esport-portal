package com.esportportal.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/test/pandascore", "/api/test/pandascore/incidents","/api/test/pandascore/incidents/raw","/api/test/pandascore/additions","/api/test/pandascore/match","/api/test/pandascore/changes/check").permitAll()
                    .requestMatchers("/api/test/pandascore/past").permitAll()
                    .requestMatchers("/api/test/pandascore/running").permitAll()
                    .requestMatchers("/api/test/pandascore/deletions").permitAll()

                    .anyRequest().authenticated()
            }

            .formLogin { }

        return http.build()
    }
}