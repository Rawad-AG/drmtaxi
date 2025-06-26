package com.drmtaxi.drm_taxi.Utils;

import java.time.LocalDate;

public record Car(
        String company,
        String model,
        LocalDate year,
        String licenseNumber,
        String color) {
}