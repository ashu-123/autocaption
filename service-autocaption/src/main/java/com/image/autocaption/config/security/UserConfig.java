package com.image.autocaption.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    private static final String ALLOWED_USERNAME = "ashu123";
    private static final String ALLOWED_PASSWORD = "pass123";
    private static final String USER = "USER";
    private static final String NOT_ALLOWED_USER = "ashu456";
    private static final String ADMIN = "ADMIN";
    private static final String NOT_ALLOWED_PASSWORD = "pass456";

    private static final String GOOGLE_ALLOWED_USER = "ashutoshmishra464@gmail.com";

    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails allowedUser = User.builder()
                .username(ALLOWED_USERNAME)
                .roles(USER)
                .password(passwordEncoder.encode(ALLOWED_PASSWORD))
                .build();

        UserDetails notAllowedUser = User.builder()
                .username(NOT_ALLOWED_USER)
                .roles(ADMIN)
                .password(passwordEncoder.encode(NOT_ALLOWED_PASSWORD))
                .build();

        UserDetails googleAllowedUser = User.builder()
                .username(GOOGLE_ALLOWED_USER)
                .roles(USER)
                .password(passwordEncoder.encode(ALLOWED_PASSWORD))
                .build();

        return new InMemoryUserDetailsManager(allowedUser, notAllowedUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
