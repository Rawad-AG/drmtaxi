package com.drmtaxi.drm_taxi.Exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadInputException extends AppException {

    public BadInputException(String message) {
        super(message);
    }

}
