package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Utils.AuthVia;
import com.drmtaxi.drm_taxi.Utils.Roles;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Auth")
@Table(name = "auths")
public class AuthEntity {

    @Id
    @SequenceGenerator(name = "auths_id_sequence", sequenceName = "auths_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auths_id_sequence")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "auth", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoleEntity> roles;

    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthProviderEntity authProvider;

    private boolean enabled = false;
    private boolean deleted = false;
    private boolean locked = false;
    private Instant lockedUntil;
    private String lockedFor;

    private String password;
    private Instant passwordUpdatedAt;
    private String passwordResetToken;
    private Instant passwordResetTokenExpiresAt;
    private Integer passwordResetTokenAttempts = 0;

    private String emailVerificationToken;
    private Instant emailVerificationTokenExpiresAt;
    private Integer emailVerificationTokenAttempts = 0;

    private Integer phoneNumberVerificationToken;
    private Instant phoneNumberVerificationTokenExpiresAt;
    private Integer phoneNumberVerificationTokenAttempts = 0;

    private boolean emailVerified = false;
    private boolean phoneNumberVerified = false;

    @Enumerated(EnumType.STRING)
    private AuthVia authVia;

    private Integer loginAttempts = 0;
    private Instant lastSession;

    private Integer accessTokenVersion = 0;
    private Integer refreshTokenVersion = 0;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

    public AuthEntity(UserEntity user, String password, boolean isDriver) {
        this.user = user;
        this.password = password;
        this.roles = List.of(new RoleEntity(this, isDriver ? Roles.DRIVER : Roles.CLIENT));
    }

    public void IncEmailVerificationAttempts() {
        this.emailVerificationTokenAttempts++;
    }

    public void IncPhoneNumberVerificationAttempts() {
        this.phoneNumberVerificationTokenAttempts++;
    }

    public void IncResetPasswordAttempts() {
        this.passwordResetTokenAttempts++;
    }

    public void setVerificationEmailToken(String token, long expiresAfter) {
        this.emailVerificationToken = token;
        this.emailVerificationTokenAttempts = 0;
        this.emailVerificationTokenExpiresAt = Instant.now().plusMillis(expiresAfter);
    }

    public void setVerificationPhoneNumberToken(int code, long expiresAfter) {
        this.phoneNumberVerificationToken = code;
        this.phoneNumberVerificationTokenAttempts = 0;
        this.phoneNumberVerificationTokenExpiresAt = Instant.now().plusMillis(expiresAfter);
    }

    public void resetEmailVerificationToken() {
        this.emailVerificationToken = null;
        this.emailVerificationTokenAttempts = 0;
        this.emailVerificationTokenExpiresAt = null;
    }

    public void resetPhoneNumberVerificationToken() {
        this.phoneNumberVerificationToken = null;
        this.phoneNumberVerificationTokenAttempts = 0;
        this.phoneNumberVerificationTokenExpiresAt = null;
    }

    public void lock(String lockedFor, long duration) {
        this.locked = true;
        this.lockedFor = lockedFor;
        this.lockedUntil = Instant.now().plusMillis(duration);
    }

    public void unLock() {
        this.locked = false;
        this.lockedFor = null;
        this.lockedUntil = null;
    }

    public void IncRefreshTokenVersion() {
        this.refreshTokenVersion++;
    }

    public void IncAccessTokenVersion() {
        this.accessTokenVersion++;
    }

    public void IncLoginAttempts() {
        this.loginAttempts++;
    }

}
