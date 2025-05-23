package com.drmtaxi.drm_taxi.Configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
@Setter
@ConfigurationProperties(prefix = "app")
public class PropertiesProvider {
    private long verify_duration;
    private long access_token_duration;
    private long refresh_token_duration;
    private String email;
    private String jwt_key;

    public long verifyDuration() {
        return verify_duration;
    }

    public long accessTokenDuration() {
        return access_token_duration;
    }

    public long refreshTokenDuration() {
        return refresh_token_duration;
    }

    public String email() {
        return email;
    }

    public String jwtKey() {
        return jwt_key;
    }

}
