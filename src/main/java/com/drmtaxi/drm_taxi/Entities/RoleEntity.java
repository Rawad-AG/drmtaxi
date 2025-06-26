package com.drmtaxi.drm_taxi.Entities;

import com.drmtaxi.drm_taxi.Utils.Roles;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Role")
@Table(name = "roles")
public class RoleEntity {

    @Id
    @SequenceGenerator(name = "roles_id_sequence", sequenceName = "roles_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_sequence")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public RoleEntity(AuthEntity auth, Roles role) {
        this.auth = auth;
        this.role = role;
    }

}
