package com.drmtaxi.drm_taxi.Exceptions.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.AppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberException extends AppException {
    public InvalidPhoneNumberException() {
        super("invalid Phone number format, phone number should starts with 09, +963 or 963");
    }

}
