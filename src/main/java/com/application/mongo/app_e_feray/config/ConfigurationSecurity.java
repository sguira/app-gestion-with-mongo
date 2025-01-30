package com.application.mongo.app_e_feray.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.application.mongo.app_e_feray.filters.JWTFilter;
import com.application.mongo.app_e_feray.services.JWTUtils;
import com.application.mongo.app_e_feray.services.UserDetailsCustom;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class ConfigurationSecurity {

    private final UserDetailsCustom userDetailsServiceCustom;
    private final JWTUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
                AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsServiceCustom).passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(h -> h.disable()).cors(c -> c.configurationSource(configurationSource()))
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/api/v1/auth/*").permitAll()
                                .requestMatchers("/api/v1/docs/**", "api/v1/swagger-ui/**", "swagger-ui/**",
                                        "/swagger-ui/index.html",
                                        "/v3/api-docs/**")
                                .permitAll()

                                .anyRequest().authenticated())

                // .formLogin(AbstractHttpConfigurer::disable)

                .addFilterBefore(new JWTFilter(userDetailsServiceCustom, jwtUtils),
                        UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}