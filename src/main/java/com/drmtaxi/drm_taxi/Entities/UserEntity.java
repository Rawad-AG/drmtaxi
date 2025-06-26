package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.InvalidEmailException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.InvalidPhoneNumberException;
import com.drmtaxi.drm_taxi.Utils.Checker;
import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Language;
import com.drmtaxi.drm_taxi.Utils.Location;
import com.drmtaxi.drm_taxi.Utils.Theme;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "users")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "users_id_sequence", sequenceName = "users_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_sequence")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean driver;

    @Enumerated(EnumType.STRING)
    private Theme theme = Theme.LIGHT;
    @Enumerated(EnumType.STRING)
    private Language language = Language.ENGLICH;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthEntity auth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverEntity driverInfo;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public UserEntity(String firstName, String lastName, String email, String phoneNumber, Gender gender,
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = Checker.parsePhoneNumber(phoneNumber);
        this.gender = gender;
        this.driver = false;
        this.auth = new AuthEntity(this, password, false);

    }

    public UserEntity(String firstName, String lastName, String phoneNumber, Gender gender, String password,
            String fatherName, String nationaleId,
            String company, String model, LocalDate year, String licenseNumber, String color,
            Location livingLocation, Location workingLocation, LocalDate birthDate, Location birthLocation,
            int startWorkingHour, int endWorkingHour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = null;
        this.phoneNumber = Checker.parsePhoneNumber(phoneNumber);
        this.gender = gender;
        this.driver = true;
        this.auth = new AuthEntity(this, password, true);
        this.driverInfo = new DriverEntity(this, fatherName, nationaleId,
                company, model, year, licenseNumber, color,
                livingLocation, workingLocation, birthDate, birthLocation,
                startWorkingHour, endWorkingHour);

    }

    public static UserEntity registerClientByEmail(String firstName, String lastName, String email, Gender gender,
            String password) {
        if (!Checker.isValidEmail(email))
            throw new InvalidEmailException();
        return new UserEntity(firstName, lastName, email, null, gender, password);
    }

    public static UserEntity registerClientByPhoneNumber(String firstName, String lastName, String phoneNumber,
            Gender gender, String password) {
        if (!Checker.isValidPhone(phoneNumber, "SY"))
            throw new InvalidPhoneNumberException();

        return new UserEntity(firstName, lastName, null, phoneNumber, gender, password);
    }

    public static UserEntity registerDriver(String firstName, String lastName, String phoneNumber,
            Gender gender, String password,
            String fatherName, String nationaleId,
            String company, String model, LocalDate year, String licenseNumber, String color,
            Location livingLocation, Location workingLocation, LocalDate birthDate, Location birthLocation,
            int startWorkingHour, int endWorkingHour) {
        if (!Checker.isValidPhone(phoneNumber, "SY"))
            throw new InvalidPhoneNumberException();

        return new UserEntity(firstName, lastName, phoneNumber, gender, password,
                fatherName, nationaleId,
                company, model, year, licenseNumber, color,
                livingLocation, workingLocation, birthDate, birthLocation,
                startWorkingHour, endWorkingHour);
    }

    public String getUsername() {
        return this.email != null ? this.email : this.phoneNumber;
    }

}
