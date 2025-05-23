package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_phone_number", columnList = "phoneNumber"),
        @Index(name = "idx_user_firstName", columnList = "firstName")
})
public class UserEntity {

    @Id
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String email;

    private String phoneNumber;

    private boolean driver = false;

    private boolean deleted = false;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthEntity auth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverInfoEntity driverInfo;

    @PrePersist
    protected void onCreate() {
    }

    @PreUpdate
    protected void onUpdate() {
    }

    public UserEntity(String firstName, String lastName, Gender gender, String email, String phoneNumber,
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.auth = new AuthEntity(this, password);
    }

    public UserEntity(
            String firstName, String lastName, Gender gender, String phoneNumber, String password,
            String licenseNumber, String carCompany, String carModel, String carNumber,
            String carColor, long idNumber, Location livingLocation, String fatherName,
            LocalDateTime dateOfBirth, Location locationOfBirth,
            LocalTime startWorkHour, LocalTime endWorkHour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.auth = new AuthEntity(this, password);
        this.driver = true;
        this.driverInfo = new DriverInfoEntity(this, licenseNumber, carCompany, carModel, carNumber, carColor, idNumber,
                livingLocation, fatherName, dateOfBirth, locationOfBirth, startWorkHour, endWorkHour);

    }

    public String getUsername() {
        if (this.email != null && !this.email.isEmpty())
            return this.email;
        else
            return this.phoneNumber;
    }

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
                + ", email=" + email + ", phoneNumber=" + phoneNumber + ", isDriver=" + driver + ", isDeleted="
                + deleted + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (gender != other.gender)
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (driver != other.driver)
            return false;
        if (deleted != other.deleted)
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + (driver ? 1231 : 1237);
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }

}
