package com.drmtaxi.drm_taxi.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.drmtaxi.drm_taxi.DTOs.SuccessResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/user")
public class RideController {
    @PostMapping("/ride")
    public ResponseEntity<SuccessResponse> bookImmediateRide(@Valid @RequestBody String data) {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(data));
    }

}
