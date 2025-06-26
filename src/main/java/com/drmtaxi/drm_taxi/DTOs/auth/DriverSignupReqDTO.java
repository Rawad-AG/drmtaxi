package com.drmtaxi.drm_taxi.DTOs.auth;

import java.time.LocalDate;

import com.drmtaxi.drm_taxi.Utils.Car;
import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record DriverSignupReqDTO(
                @NotNull(message = "Phone number is required") String phoneNumber,

                @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password,

                @NotBlank(message = "First name is required") String firstName,

                @NotBlank(message = "Last name is required") String lastName,

                @NotBlank(message = "Father name is required") String fatherName,

                @NotNull(message = "Gender is required") Gender gender,

                @NotNull Car car,

                @Size(max = 20, message = "National ID too long") String nationaleId,

                @NotNull(message = "Living location is required") Location livingLocation,

                @NotNull(message = "Birth location is required") Location birthLocation,

                @NotNull(message = "Work location is required") Location workLocation,

                @Past(message = "Birth date must be in the past") @NotNull(message = "Birth date is required") LocalDate birthDate,

                @Min(value = 0, message = "Start working hour must be between 0 and 23") @Max(value = 23, message = "Start working hour must be between 0 and 23") int startWorkingHour,

                @Min(value = 0, message = "End working hour must be between 0 and 23") @Max(value = 23, message = "End working hour must be between 0 and 23") int endWorkingHour) {
}
