package com.drmtaxi.drm_taxi.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.drmtaxi.drm_taxi.Utils.Car;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "DriverInfo")
@Table(name = "drivers_info")
public class DriverInfoEntity {

        @Id
        @SequenceGenerator(name = "driver_id_sequence", sequenceName = "driver_id_sequence", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_id_sequence")
        private Long id;

        @OneToOne(optional = false)
        @JoinColumn(name = "user_id")
        private UserEntity user;

        @Embedded
        private Car car;

        private LocalDateTime startedWorkingAt;

        @Column(name = "id_number", unique = true, nullable = false)
        private long idNumber;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "living_latitude")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "living_longitude"))
        })
        private Location livingLocation;

        private String fatherName;

        private LocalDateTime dateOfBirth;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "latitude", column = @Column(name = "birth_latitude")),
                        @AttributeOverride(name = "longitude", column = @Column(name = "birth_longitude"))
        })
        private Location locationOfBirth;

        private LocalTime startWorkHour;

        private LocalTime endWorkHour;

        private boolean available = false;

        private double amountOwed;

        private LocalDate paymentDeadline;

        private boolean inWork = false;

        @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
        private List<DriverPaymentsEntity> payments;

        public DriverInfoEntity(UserEntity user, String licenseNumber, String carCompany, String carModel,
                        String carNumber,
                        String carColor, long idNumber, Location livingLocation, String fatherName,
                        LocalDateTime dateOfBirth,
                        Location locationOfBirth, LocalTime startWorkHour, LocalTime endWorkHour) {
                this.user = user;
                this.car = new Car(licenseNumber, carCompany, carModel, carNumber, carColor);
                this.idNumber = idNumber;
                this.livingLocation = livingLocation;
                this.fatherName = fatherName;
                this.dateOfBirth = dateOfBirth;
                this.locationOfBirth = locationOfBirth;
                this.startWorkHour = startWorkHour;
                this.endWorkHour = endWorkHour;
                this.payments = new ArrayList<>();
        }

        @Override
        public String toString() {
                return "DriverInfoEntity [id=" + id + ", fullName=" + user.getFirstName() + " " + fatherName + " "
                                + user.getLastName() + ", licenseNumber=" + car.getLicenseNumber() + ", carCompany="
                                + car.getCarCompany()
                                + ", carModel=" + car.getCarModel() + ", carNumber=" + car.getCarNumber()
                                + ", carColor=" + car.getCarColor()
                                + ", startedWorkingAt=" + startedWorkingAt + ", idNumber=" + idNumber
                                + ", livingLocation="
                                + livingLocation + ", dateOfBirth=" + dateOfBirth + ", locationOfBirth="
                                + locationOfBirth + ", startWorkHour=" + startWorkHour + ", endWorkHour=" + endWorkHour
                                + ", available="
                                + available + ", amountOwed=" + amountOwed + ", paymentDeadline=" + paymentDeadline
                                + ", inWork="
                                + inWork + "]";
        }

}
