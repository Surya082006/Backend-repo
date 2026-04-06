package com.klu.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import com.klu.security.JwtFilter;

@Configuration
public class SecurityConfig {

    // 🔐 Password Encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔥 MAIN SECURITY CONFIG
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            // 🔥 ENABLE CORS (VERY IMPORTANT)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ❌ Disable CSRF
            .csrf(csrf -> csrf.disable())

            // 🔐 Authorization Rules
            .authorizeHttpRequests(auth -> auth
                // ✅ Public APIs
                .requestMatchers("/api/auth/**").permitAll()

                // 🔥 ROLE BASED ACCESS
                .requestMatchers("/api/file/upload/course/**").hasAuthority("EDUCATOR")
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

    // 🔥🔥🔥 CORS CONFIGURATION (THIS FIXES YOUR ERROR)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);

        // ✅ Allow your frontend
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:5174"); // ✅ ADD THIS
        configuration.addAllowedOriginPattern("*"); // ✅ BEST FOR DEV

        // ✅ Allow all headers
        configuration.addAllowedHeader("*");

        // ✅ Allow all methods (GET, POST, PUT, DELETE)
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}