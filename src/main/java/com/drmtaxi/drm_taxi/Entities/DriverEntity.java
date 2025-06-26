package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Utils.DriverStatus;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Driver")
@Table(name = "drivers")
public class DriverEntity {
        @Id
        @SequenceGenerator(name = "drivers_id_sequence", sequenceName = "drivers_id_sequence", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drivers_id_sequence")
        private Long id;

        @OneToOne(optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        private UserEntity user;

        private String fatherName;

        @OneToOne(mappedBy = "driver", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
        private CarEntity car;

        private Instant startWorkingAt;

        private String nationaleId;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "living_lat")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "living_lng"))
        })
        private Location livingLocation;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "working_lat")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "working_lng"))
        })
        private Location workingLocation;

        private LocalDate birthDate;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "birth_lat")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "birth_lng"))
        })
        private Location birthLocation;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "current_lat")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "current_lng"))
        })
        private Location currentLocation;

        private int startWorkingHour;
        private int endWorkingHour;

        private boolean inWork = false;

        private int totalRatingSum = 0;
        private int ratingCount = 0;

        @Enumerated(EnumType.STRING)
        private DriverStatus status;

        @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DriverFinancialEntity> financials;

        @CreationTimestamp
        private Instant createdAt;

        @UpdateTimestamp
        private Instant updatedAt;

        public DriverEntity(UserEntity user,
                        String fatherName, String nationaleId,
                        String company, String model, LocalDate year, String licenseNumber, String color,
                        Location livingLocation, Location workingLocation, LocalDate birthDate, Location birthLocation,
                        int startWorkingHour, int endWorkingHour) {
                this.user = user;
                this.fatherName = fatherName;
                this.car = new CarEntity(this, company, model, year, licenseNumber, color);
                this.nationaleId = nationaleId;
                this.livingLocation = livingLocation;
                this.workingLocation = workingLocation;
                this.birthDate = birthDate;
                this.birthLocation = birthLocation;
                this.startWorkingHour = startWorkingHour;
                this.endWorkingHour = endWorkingHour;
                this.status = DriverStatus.OFFLINE;
                this.financials = List.of();
        }

}
