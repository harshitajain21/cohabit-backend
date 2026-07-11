package com.cohabit.cohabitbackend.config;

import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.security.JwtAuthenticationEntryPoint;
import com.cohabit.cohabitbackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import java.util.Collections;

/**
 * Configures stateless Spring Security and JWT authentication for the API.
 */
@Configuration
public class SecurityConfig {

    /**
     * Builds the HTTP security filter chain.
     *
     * @param http Spring Security HTTP builder
     * @param jwtAuthenticationFilter JWT request filter
     * @param jwtAuthenticationEntryPoint unauthorized response handler
     * @return configured security filter chain
     * @throws Exception when Spring Security cannot build the chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/auth/verify", "/auth/resend-verification").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides BCrypt password hashing.
     *
     * @return password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Loads users for Spring Security by IIT email.
     *
     * @param userRepository user repository
     * @return user details service
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByIitEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getIitEmail(),
                        user.getPassword(),
                        user.isEmailVerified(),
                        true,
                        true,
                        true,
                        Collections.emptyList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
