package com.drmtaxi.drm_taxi.DTOs.auth;

import jakarta.validation.constraints.NotBlank;

public record PhoneNumberVerificationReqDTO(
                @NotBlank(message = "phone number is required") String phoneNumber,
                int code) {

}
