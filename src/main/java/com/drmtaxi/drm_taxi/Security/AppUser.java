package com.drmtaxi.drm_taxi.Security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.drmtaxi.drm_taxi.Entities.UserEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppUser implements UserDetails {
    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuth().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }

    public Integer getTokenVersion() {
        return this.user.getAuth().getAccessTokenVersion();
    }

    @Override
    public String getPassword() {
        return this.user.getAuth().getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.user.getAuth().isDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.getAuth().isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getAuth().isEnabled() &&
                (!this.user.isDriver() || (this.user.isDriver() && this.user.getDriverInfo().isInWork()));
    }
}
