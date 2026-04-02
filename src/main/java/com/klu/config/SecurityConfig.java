package com.klu.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.klu.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // ✅ Public APIs
                .requestMatchers("/api/auth/**").permitAll()

                // 🔥 ROLE BASED ACCESS
                .requestMatchers("/api/educator/**").hasAuthority("EDUCATOR")
                .requestMatchers("/api/student/**").hasAuthority("STUDENT")

                // ✅ All other requests require login
                .anyRequest().authenticated()
            )

            // 🔐 JWT Filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // ❌ Disable default login
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}