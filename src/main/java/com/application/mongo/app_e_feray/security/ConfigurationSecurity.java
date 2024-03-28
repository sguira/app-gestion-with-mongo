// package com.application.mongo.app_e_feray.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
// import
// org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCrypt;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// public class ConfigurationSecurity {

// // @Autowired private EntryPoi authenticationEntryPoint;

// public void configurationGlobal(AuthenticationManagerBuilder auth) throws
// Exception {
// auth.inMemoryAuthentication().withUser("sguira96@gmail.com")
// .password(passwordEncoder().encode("12345678"))
// .authorities("ADMIN");
// }

// @Bean
// SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws
// Exception {

// httpSecurity.authorizeHttpRequests(
// hhptAuth -> hhptAuth.requestMatchers("/backend/**").permitAll());
// // httpSecurity.httpBasic(customHttpBasic ->
// // customHttpBasic.authenticationEntryPoint(null));
// // httpSecurity.addFilterAfter(new CustomFilter(),
// // BasicAuthenticationFilter.class);
// return httpSecurity.build();
// }

// @Bean
// PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// }
