package com.drmtaxi.drm_taxi.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drmtaxi.drm_taxi.DTOs.LoginDTO;
import com.drmtaxi.drm_taxi.DTOs.SignupDriverDTO;
import com.drmtaxi.drm_taxi.DTOs.SignupUserDTO;
import com.drmtaxi.drm_taxi.DTOs.SuccessResponse;
import com.drmtaxi.drm_taxi.Services.UserService;
import com.drmtaxi.drm_taxi.Utils.Messager;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService service;

    @PostMapping("/user/signup")
    public ResponseEntity<SuccessResponse> userSignup(@Valid @RequestBody SignupUserDTO user) {
        service.signupUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("null"));
    }

    @PostMapping("/driver/signup")
    public ResponseEntity<SuccessResponse> driverSignup(@Valid @RequestBody SignupDriverDTO driver) {
        service.signupDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("null"));
    }

    @GetMapping("/verifyAccount")
    public ResponseEntity<SuccessResponse> verifyAccount(@RequestParam String token,
            @RequestParam(required = false) String username) {
        service.verifyAccound(token, username);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("null"));
    }

    @GetMapping("/verifyAccount/resend")
    public ResponseEntity<SuccessResponse> resendAccountVerificationToken(@RequestParam("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("null"));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginDTO credintials) {
        String token = service.login(credintials);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(null, Map.of("token", token)));
    }

    @GetMapping("/refresh")
    public ResponseEntity<SuccessResponse> getAccessToken(@RequestParam String token) {
        String accessToken = service.getAccessToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(null, Map.of("token", accessToken)));
    }

    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestParam String token) {
        service.logout(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(Messager.logout()));
    }

}
