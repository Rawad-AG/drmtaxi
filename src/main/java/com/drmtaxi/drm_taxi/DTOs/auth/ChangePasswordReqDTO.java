package com.drmtaxi.drm_taxi.DTOs.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordReqDTO(
        @NotBlank(message = "old password is required") @Size(min = 8, message = "invalid old password") String oldPassword,
        @NotBlank(message = "new password is required") @Size(min = 8, message = "new password must be at least 8 characters long") String newPassword) {

}
