package com.drmtaxi.drm_taxi.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drmtaxi.drm_taxi.Services.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return service.loadUserByUsername(username);
    }

}
