package com.drmtaxi.drm_taxi.Configs;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.Setter;

@Component
@Setter
@ConfigurationProperties(prefix = "app")
public class PropertiesProvider {
    private Long verify_duration;
    private Long access_token_duration;
    private Long refresh_token_duration;
    private Long verify_lock_duration;
    private String verify_lock_message;
    private Long login_lock_duration;
    private String login_lock_message;
    private Integer login_attempts;
    private Integer verify_attempts;
    private String email;
    private String jwt_key;

    public long verifyDuration() {
        return verify_duration != null ? verify_duration : 900000;
    }

    public long verifyLockDuration() {
        return verify_lock_duration != null ? verify_lock_duration : 900000;
    }

    public String verifyLockMessage() {
        return verify_lock_message != null ? verify_lock_message
                : "locked for too many wrong attempts for account-verification";
    }

    public long loginLockDuration() {
        return login_lock_duration != null ? login_lock_duration : 900000;
    }

    public String loginLockMessage() {
        return login_lock_message != null ? login_lock_message
                : "locked for too many wrong login attempts";
    }

    public int loginAttempts() {
        return login_attempts != null ? login_attempts : 3;
    }

    public int verifyAttempts() {
        return verify_attempts != null ? verify_attempts : 3;
    }

    public long accessTokenDuration() {
        return access_token_duration != null ? access_token_duration : 900000;
    }

    public long refreshTokenDuration() {
        return refresh_token_duration != null ? refresh_token_duration : 900000;
    }

    public String email() {
        return email != null ? email : "rawad.dev@example.com";
    }

    public String jwtKey() {
        return jwt_key != null ? jwt_key : getJwtKey();
    }

    private String getJwtKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());

    }

}
