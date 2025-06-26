package com.drmtaxi.drm_taxi.Exceptions.exceptions.auth;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.AppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeleteUnFreeDriverException extends AppException {
    public DeleteUnFreeDriverException() {
        super("Driver is not financial free");
    }

}
