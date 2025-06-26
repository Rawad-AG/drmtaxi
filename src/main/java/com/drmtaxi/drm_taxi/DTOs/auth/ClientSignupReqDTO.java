package com.drmtaxi.drm_taxi.DTOs.auth;

import com.drmtaxi.drm_taxi.Utils.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientSignupReqDTO(
                @Email(message = "Invalid email format") @Size(max = 255, message = "Email too long") String email,
                String phoneNumber,
                @NotBlank(message = "password is required") @Size(min = 8, message = "password must be at least 8 characters long") String password,
                @NotBlank(message = "First name required") String firstName,
                @NotBlank(message = "Last name required") String lastName,
                @NotNull(message = "Gender required") Gender gender) {

}
