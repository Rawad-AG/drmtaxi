package com.drmtaxi.drm_taxi.Exceptions.exceptions.auth;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.AppException;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class tooManyAttemptsException extends AppException {

    public tooManyAttemptsException() {
        super("too many attempts, please try again later");
    }

}
