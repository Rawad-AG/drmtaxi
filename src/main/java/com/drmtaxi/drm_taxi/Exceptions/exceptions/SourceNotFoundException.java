package com.drmtaxi.drm_taxi.Exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SourceNotFoundException extends AppException {

    public SourceNotFoundException(String message) {
        super(message);
    }

}
