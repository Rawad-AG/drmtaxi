package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.drmtaxi.drm_taxi.Utils.AuthVia;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AuthProvider")
@Table(name = "auth_providers")
public class AuthProviderEntity {
    @Id
    @SequenceGenerator(name = "auth_providers_id_sequence", sequenceName = "auth_providers_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_providers_id_sequence")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;

    @Enumerated(EnumType.STRING)
    private AuthVia providerName;

    private String providerId;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

}
