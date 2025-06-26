package com.drmtaxi.drm_taxi.DTOs.auth;

import java.time.Instant;
import java.util.List;

import com.drmtaxi.drm_taxi.Entities.DriverEntity;
import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Utils.DriverStatus;
import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Location;
import com.drmtaxi.drm_taxi.Utils.Roles;

public record UserProfileDTO(
        Long id,
        String firstName,
        String lastName,
        List<Roles> roles,
        Gender gender,
        String email,
        String phoneNumber,
        Instant createdAt,
        Instant updatedAt,
        Long driverId,
        String fatherName,
        String licenseNumber,
        Instant startWorkingAt,
        Location birthLocation,
        Location workLocation,
        Location livingLocation,
        String carCompany,
        String carColor,
        int startWorkingHour,
        int endWorkingHour,
        int totalRatingSum,
        int ratingCount,
        DriverStatus status) {

    public static UserProfileDTO fromDriverEntity(DriverEntity driver) {
        return new UserProfileDTO(
                driver.getUser().getId(),
                driver.getUser().getFirstName(),
                driver.getUser().getLastName(),
                driver.getUser().getAuth().getRoles().stream().map(el -> el.getRole()).toList(),
                driver.getUser().getGender(),
                driver.getUser().getEmail(),
                driver.getUser().getPhoneNumber(),
                driver.getCreatedAt(),
                driver.getUpdatedAt(),
                driver.getId(),
                driver.getFatherName(),
                driver.getCar().getLicenseNumber(),
                driver.getStartWorkingAt(),
                driver.getBirthLocation(),
                driver.getWorkingLocation(),
                driver.getLivingLocation(),
                driver.getCar().getCompany(),
                driver.getCar().getColor(),
                driver.getStartWorkingHour(),
                driver.getEndWorkingHour(),
                driver.getTotalRatingSum(),
                driver.getRatingCount(),
                driver.getStatus());

    }

    public static UserProfileDTO fromUserEntity(UserEntity user) {
        return new UserProfileDTO(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAuth().getRoles().stream().map(el -> el.getRole()).toList(),
                user.getGender(),
                user.getEmail(),
                user.getPhoneNumber(), user.getCreatedAt(), user.getUpdatedAt(),
                null, null, null, null,
                null, null, null,
                null, null, 0, 0, 0, 0, null);
    }
}
