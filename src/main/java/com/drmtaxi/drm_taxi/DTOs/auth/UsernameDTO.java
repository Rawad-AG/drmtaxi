package com.drmtaxi.drm_taxi.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsernameDTO(
        @Email(message = "Invalid email format") @Size(max = 255, message = "Email too long") String email,
        String phoneNumber) {

}
