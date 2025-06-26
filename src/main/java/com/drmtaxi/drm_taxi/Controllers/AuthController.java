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

import com.drmtaxi.drm_taxi.Configs.PropertiesProvider;
import com.drmtaxi.drm_taxi.DTOs.SuccessResponse;
import com.drmtaxi.drm_taxi.DTOs.auth.ClientSignupReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.DriverSignupReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.LoginReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.PhoneNumberVerificationReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.UsernameDTO;
import com.drmtaxi.drm_taxi.Services.UserService;
import com.drmtaxi.drm_taxi.Utils.Checker;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService service;
    private final PropertiesProvider provider;

    @PostMapping("/register-client")
    public ResponseEntity<SuccessResponse> registerClient(@Valid @RequestBody ClientSignupReqDTO user) {
        service.registerClient(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("user registered successfully"));
    }

    @PostMapping("/register-driver")
    public ResponseEntity<SuccessResponse> registerDriver(@Valid @RequestBody DriverSignupReqDTO driver) {
        service.registerDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("your account registered successfully"));
    }

    @GetMapping("/verify/email")
    public ResponseEntity<SuccessResponse> verifyEmail(@RequestParam String token, @RequestParam int id) {
        service.verifyEmail(token, id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("email virified successfully"));
    }

    @PostMapping("/verify/phone")
    public ResponseEntity<SuccessResponse> verifyPhoneNumber(@RequestBody PhoneNumberVerificationReqDTO req) {
        service.verifyPhoneNumber(req.code(), Checker.parsePhoneNumber(req.phoneNumber()));
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("account verified successfully"));
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<SuccessResponse> resendVerification(@RequestBody UsernameDTO username) {
        service.resendAccountVerification(username);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    @PostMapping("/password/forget")
    public ResponseEntity<SuccessResponse> forgetPassword() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    @PostMapping("/password/verify")
    public ResponseEntity<SuccessResponse> verifyResetPasswordCode() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<SuccessResponse> resetPassword() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    @PostMapping("/password/change")
    public ResponseEntity<SuccessResponse> changePassword() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginReqDTO req, HttpServletResponse res) {
        String token = service.login(req);

        Cookie cookie = new Cookie("rtn", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge((int) provider.refreshTokenDuration());
        res.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("", Map.of("refreshToken", token)));
    }

    @GetMapping("/refresh")
    public ResponseEntity<SuccessResponse> refresh(HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = getRefreshToken(req);
        String accessToken = service.getAccessToken(refreshToken);

        Cookie cookie = new Cookie("actn", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge((int) provider.accessTokenDuration());
        res.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("", Map.of("accessToken", accessToken)));
    }

    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(HttpServletRequest req) {
        String token = getRefreshToken(req);
        service.logout(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    }

    private String getRefreshToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());

        }

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("rtn")) {
                return cookie.getValue();
            }
        }

        return null;
    }

}
