package com.drmtaxi.drm_taxi.Utils;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Car {
    private String licenseNumber;
    private String carCompany;
    private String carModel;
    private String carNumber;
    private String carColor;

    public Car(String licenseNumber, String carCompany, String carModel, String carNumber, String carColor) {
        this.licenseNumber = licenseNumber;
        this.carCompany = carCompany;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carColor = carColor;
    }

}
