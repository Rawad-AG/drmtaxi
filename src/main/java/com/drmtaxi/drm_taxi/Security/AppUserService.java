package com.drmtaxi.drm_taxi.Security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Repositories.UserRepo;
import com.drmtaxi.drm_taxi.Utils.Messager;
import com.drmtaxi.drm_taxi.Utils.PhoneNumberChecker;
import lombok.AllArgsConstructor;



@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService{
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null) throw new UsernameNotFoundException(Messager.userNotFound(username));
        if(username.contains("@")) {
           Optional<UserEntity> user = userRepo.findByEmail(username);
           if(user.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(username));
           return new AppUser(user.get());
        }

        if(PhoneNumberChecker.isValidPhone(username, "SY")) {
            Optional<UserEntity> user = userRepo.findByPhoneNumber(username);
            if(user.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(username));
            return new AppUser(user.get());
        }
        throw new UsernameNotFoundException(Messager.userNotFound(username));
    }
    
}
