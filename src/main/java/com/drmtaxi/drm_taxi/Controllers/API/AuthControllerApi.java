package com.drmtaxi.drm_taxi.Controllers.API;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drmtaxi.drm_taxi.DTOs.SuccessResponse;
import com.drmtaxi.drm_taxi.DTOs.UsernamePasswordDTO;
import com.drmtaxi.drm_taxi.Services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerApi {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> basicLogin(@Valid @RequestBody UsernamePasswordDTO credintials) {
        service.usernamePasswordVerify(credintials.username(), credintials.password());
        return ResponseEntity.ok(new SuccessResponse(""));
    }
    
    
}
