package com.drmtaxi.drm_taxi.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginReqDTO(
        @Email(message = "Invalid email format") @Size(max = 255, message = "Email too long") String email,
        String phoneNumber,
        @NotBlank(message = "password is required") @Size(min = 8, message = "password must be at least 8 characters long") String password) {

}
