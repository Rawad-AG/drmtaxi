package com.drmtaxi.drm_taxi.Utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.drmtaxi.drm_taxi.Entities.UserEntity;

@Component
public class Authenticator {

    public boolean isUserSignable(Optional<UserEntity> user) {
        return user.isEmpty() ||
                !user.get().getAuth().isEnabled();
    }

    public boolean isUserLoginable(UserEntity user) {
        return !user.getAuth().isLocked() &&
                user.getAuth().isEnabled() &&
                !user.getAuth().isDeleted();
    }

}
