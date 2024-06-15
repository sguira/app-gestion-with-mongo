// package com.application.mongo.app_e_feray.security;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// import jakarta.xml.ws.http.HTTPException;

// @Configuration
// @EnableWebSecurity
// class ConfigurationSecurity{

// @Bean
// SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// HTTPException{

// http.authorizeRequests((authorize)->authorize.anyRequest().authenticated())
// .httpBasic(withDefaults())
// .formLogin(withDefaults());

// return http.build();
// }

// }