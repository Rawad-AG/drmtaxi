package com.drmtaxi.drm_taxi.Utils;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {
    public long latitude;
    public long longitude;
}
