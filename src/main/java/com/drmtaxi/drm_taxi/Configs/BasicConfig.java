package com.drmtaxi.drm_taxi.Configs;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class BasicConfig {
    private final PropertiesProvider provider;

    @Bean
    SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.provider.jwtKey()));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
