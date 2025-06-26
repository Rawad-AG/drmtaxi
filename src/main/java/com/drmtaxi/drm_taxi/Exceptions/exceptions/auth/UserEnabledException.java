package com.drmtaxi.drm_taxi.Exceptions.exceptions.auth;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserEnabledException extends AppException {
    public UserEnabledException() {
        super("user is already enabled");
    }

}
