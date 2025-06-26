package com.drmtaxi.drm_taxi.DTOs.auth;

import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateInfoReqDTO(
        String firstName,
        String lastName,
        Location livingLocation,
        Location workLocation,
        @Min(value = 0, message = "Start working hour must be between 0 and 23") @Max(value = 23, message = "Start working hour must be between 0 and 23") int startWorkingHour,
        @Min(value = 0, message = "End working hour must be between 0 and 23") @Max(value = 23, message = "End working hour must be between 0 and 23") int endWorkingHour) {

}
