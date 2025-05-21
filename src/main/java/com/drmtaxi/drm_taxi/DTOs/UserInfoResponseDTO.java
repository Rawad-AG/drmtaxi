package com.drmtaxi.drm_taxi.DTOs;

import java.time.LocalDate;
import java.util.List;

import com.drmtaxi.drm_taxi.Utils.UserRoles;

public record UserInfoResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthDate,
    List<UserRoles> roles
) {
    
}
