package com.drmtaxi.drm_taxi.Exceptions.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.AppException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends AppException {
    public TokenExpiredException() {
        super("token expired");
    }

}
