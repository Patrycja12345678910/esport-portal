package com.esportportal.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("test")
class TestSecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/api/test/cito/sync", "/api/test/cito/approve")
            }
            .httpBasic { }
            .formLogin { }

        return http.build()
    }
}