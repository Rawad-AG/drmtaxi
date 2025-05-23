package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Utils.UserRoles;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
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
@Entity(name = "Auth")
@Table(name = "auths")
public class AuthEntity {
    @jakarta.persistence.Id
    @SequenceGenerator(name = "auth_id_sequence", sequenceName = "auth_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_id_sequence")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "auth_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<UserRoles> roles;

    private boolean enable = false;

    private boolean locked = false;
    private String lockedFor;

    private String password;
    private String prevPassword;
    private Instant passwordUpdatedAt = Instant.now();
    private String passwordResetUUID;
    private Instant passwordResetUUIDExpiresAt;

    private double tokensVersion = 1;

    private String accountVerificationUUID;
    private Instant accountVerificationUUIDExpiresAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        prevPassword = password;
    }

    @PreUpdate
    protected void onUpdate() {
        if (!prevPassword.equals(password)) {
            passwordUpdatedAt = Instant.now();
            prevPassword = password;
        }
    }

    public AuthEntity(UserEntity user, String password) {
        this.user = user;
        this.password = password;
        this.prevPassword = password;
        this.enable = false;
        this.locked = false;
        this.roles = new ArrayList<>();
        roles.add(UserRoles.USER);
    }

    public AuthEntity(UserEntity user, String password, boolean isDriver) {
        this.user = user;
        this.password = password;
        this.prevPassword = password;
        this.enable = false;
        this.locked = false;
        if (isDriver) {
            this.roles = new ArrayList<>();
            roles.add(UserRoles.DRIVER);
        } else {
            this.roles = new ArrayList<>();
            roles.add(UserRoles.USER);
        }
    }

    public void setPassword(String newPass) {
        this.password = newPass;
        this.prevPassword = newPass;
        this.updatedAt = Instant.now();
        this.passwordUpdatedAt = Instant.now();
    }

    public void setPasswordResetUUID(String token, long expiresAfter) {
        this.passwordResetUUID = token;
        this.passwordResetUUIDExpiresAt = Instant.now().plusMillis(expiresAfter);
    }

    public void setAccountVerificationUUID(String token, long expiresAfter) {
        this.accountVerificationUUID = token;
        this.accountVerificationUUIDExpiresAt = Instant.now().plusMillis(expiresAfter);
    }

    @Override
    public String toString() {
        return "AuthEntity [user=" + user.getFirstName() + " " + user.getLastName()
                + ", id=" + id
                + ", roles=" + roles
                + ", enable=" + enable
                + ", locked=" + locked
                + ", lockedFor=" + lockedFor
                + ", updatedAt=" + updatedAt + "]";
    }

}
