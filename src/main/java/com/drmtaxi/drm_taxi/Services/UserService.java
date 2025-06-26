package com.drmtaxi.drm_taxi.Services;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.drmtaxi.drm_taxi.Configs.PropertiesProvider;
import com.drmtaxi.drm_taxi.DTOs.auth.ClientSignupReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.DriverSignupReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.LoginReqDTO;
import com.drmtaxi.drm_taxi.DTOs.auth.UsernameDTO;
import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.BadInputException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.IncorrectPasswordException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.InvalidTokenException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.InvalidUsernameException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.TokenExpiredException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.UserAlreadyExistsException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.UserEnabledException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.UserLockedException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.UserNotActiveException;
import com.drmtaxi.drm_taxi.Repositories.UserRepo;
import com.drmtaxi.drm_taxi.Security.AppUser;
import com.drmtaxi.drm_taxi.Utils.Authenticator;
import com.drmtaxi.drm_taxi.Utils.Checker;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final Authenticator authenticator;
    private final EmailService mailer;
    private final PropertiesProvider provider;
    private final JwtService jwt;
    private final PasswordEncoder encoder;
    private final TaskScheduler scheduler;

    public UserDetails loadUserByUsername(String username) {
        try {
            Optional<UserEntity> opUser = findByEmail(username);
            if (opUser.isEmpty())
                throw new UsernameNotFoundException("user not found");
            return new AppUser(opUser.get());
        } catch (InvalidUsernameException e) {
            try {
                Optional<UserEntity> opUser = findByPhoneNumber(username);
                if (opUser.isEmpty())
                    throw new UsernameNotFoundException("user not found");
                return new AppUser(opUser.get());
            } catch (InvalidUsernameException exp) {
                Optional<UserEntity> opUser = repo.findById(Long.parseLong(username));
                if (opUser.isEmpty())
                    throw new UsernameNotFoundException("user not found");
                return new AppUser(opUser.get());
            }
        }
    }

    public Optional<UserEntity> findByUsername(String email, String phoneNumber) {
        if (email != null & Checker.isValidEmail(email))
            return repo.findByEmail(email);

        return findByPhoneNumber(phoneNumber);
    }

    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && Checker.isValidPhone(phoneNumber, "SY"))
            return repo.findByPhoneNumber(Checker.parsePhoneNumber(phoneNumber));

        throw new InvalidUsernameException();
    }

    public Optional<UserEntity> findByEmail(String email) {
        if (email != null && Checker.isValidEmail(email))
            return repo.findByEmail(email);

        throw new InvalidUsernameException();
    }

    private void sendEmailVerification(UserEntity user) {
        String token = UUID.randomUUID().toString();
        user.getAuth().setVerificationEmailToken(token, provider.verifyDuration());
        repo.save(user);

        scheduler.schedule(() -> {
            user.getAuth().resetEmailVerificationToken();
            repo.save(user);
        }, Instant.now().plusMillis(provider.verifyDuration()));

        try {
            mailer.sendVerifyToken(user.getEmail(), jwt.sign(token, provider.verifyDuration()), user.getId());
        } catch (MessagingException e) {
            user.getAuth().resetEmailVerificationToken();
            repo.save(user);
        }
    }

    private void sendPhoneNumberVerification(UserEntity user) {
        int token = new Random().nextInt(100000, 999999);
        user.getAuth().setVerificationPhoneNumberToken(token, provider.verifyDuration());
        repo.save(user);

        scheduler.schedule(() -> {
            user.getAuth().resetPhoneNumberVerificationToken();
            repo.save(user);
        }, Instant.now().plusMillis(provider.verifyDuration()));

        try {
            mailer.sendVerifyCode(user.getPhoneNumber(), token, user.getId());
        } catch (MessagingException e) {
            user.getAuth().resetEmailVerificationToken();
            repo.save(user);
        }
    }

    public void registerClient(ClientSignupReqDTO userReq) {
        Optional<UserEntity> opUser = findByUsername(userReq.email(), userReq.phoneNumber());

        if (!authenticator.isUserSignable(opUser))
            throw new UserAlreadyExistsException();

        if (opUser.isEmpty()) {
            if (userReq.email() != null) {
                UserEntity user = UserEntity.registerClientByEmail(userReq.firstName(), userReq.lastName(),
                        userReq.email(), userReq.gender(), encoder.encode(userReq.password()));
                sendEmailVerification(user);
            } else {
                UserEntity user = UserEntity.registerClientByPhoneNumber(userReq.firstName(), userReq.lastName(),
                        userReq.phoneNumber(), userReq.gender(), encoder.encode(userReq.password()));
                sendPhoneNumberVerification(user);
            }
            return;
        }

        UserEntity user = opUser.get();
        if (user.getEmail() != null)
            sendEmailVerification(user);

        if (user.getPhoneNumber() != null)
            sendPhoneNumberVerification(user);
    }

    public void registerDriver(DriverSignupReqDTO driverReq) {
        Optional<UserEntity> opUser = findByUsername(null, driverReq.phoneNumber());

        if (!authenticator.isUserSignable(opUser))
            throw new UserAlreadyExistsException();

        if (opUser.isEmpty()) {
            UserEntity user = UserEntity.registerDriver(driverReq.firstName(), driverReq.lastName(),
                    driverReq.phoneNumber(), driverReq.gender(), encoder.encode(driverReq.password()),
                    driverReq.fatherName(), driverReq.nationaleId(),
                    driverReq.car().company(), driverReq.car().model(), driverReq.car().year(),
                    driverReq.car().licenseNumber(),
                    driverReq.car().color(), driverReq.livingLocation(), driverReq.workLocation(),
                    driverReq.birthDate(),
                    driverReq.birthLocation(), driverReq.startWorkingHour(),
                    driverReq.endWorkingHour());

            sendPhoneNumberVerification(user);

            return;
        }

        sendPhoneNumberVerification(opUser.get());

    }

    public void verifyEmail(String token, long id) {
        UserEntity user = repo.findById(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("user not found");
        });

        if (user.getAuth().isEnabled())
            throw new UserEnabledException();

        if (user.getAuth().isLocked())
            throw new UserLockedException(user.getAuth().getLockedFor());

        String uuid = jwt.verify(token).getSubject();
        if (user.getAuth().getEmailVerificationToken() == null) {
            throw new BadInputException(
                    "please register first, if you already registered that means the verification link is expired");
        }

        if (!user.getAuth().getEmailVerificationToken().equals(uuid)) {
            user.getAuth().IncEmailVerificationAttempts();
            if (user.getAuth().getEmailVerificationTokenAttempts() > provider.verifyAttempts()) {
                user.getAuth().resetEmailVerificationToken();
                user.getAuth().lock("too many attempts for verifying the email", provider.verifyLockDuration());
                scheduler.schedule(() -> {
                    user.getAuth().unLock();
                    repo.save(user);
                }, Instant.now().plusMillis(provider.verifyLockDuration()));
            }
            repo.save(user);
            throw new InvalidTokenException();
        }

        if (user.getAuth().getEmailVerificationTokenExpiresAt().isBefore(Instant.now())) {
            user.getAuth().resetEmailVerificationToken();
            repo.save(user);
            throw new TokenExpiredException();
        }

        user.getAuth().setEnabled(true);
        user.getAuth().setDeleted(false);
        user.getAuth().resetEmailVerificationToken();
        user.getAuth().setEmailVerified(true);
        repo.save(user);

    }

    public void verifyPhoneNumber(int code, String phoneNumber) {
        UserEntity user = findByPhoneNumber(phoneNumber).orElseThrow(() -> {
            throw new UsernameNotFoundException("user not found");
        });

        if (user.getAuth().isEnabled())
            throw new UserEnabledException();

        if (user.getAuth().isLocked())
            throw new UserLockedException(user.getAuth().getLockedFor());

        if (user.getAuth().getPhoneNumberVerificationToken() == null) {
            throw new BadInputException(
                    "please register first, if you already registered that means the verification link is expired");
        }

        if (user.getAuth().getPhoneNumberVerificationToken() != code) {
            user.getAuth().IncPhoneNumberVerificationAttempts();
            if (user.getAuth().getPhoneNumberVerificationTokenAttempts() > provider.verifyAttempts()) {
                user.getAuth().resetPhoneNumberVerificationToken();
                user.getAuth().lock("too many attempts for verifying the phone number", provider.verifyLockDuration());
                scheduler.schedule(() -> {
                    user.getAuth().unLock();
                    repo.save(user);
                }, Instant.now().plusMillis(provider.verifyLockDuration()));
            }
            repo.save(user);
            throw new InvalidTokenException();
        }

        if (user.getAuth().getPhoneNumberVerificationTokenExpiresAt().isBefore(Instant.now())) {
            user.getAuth().resetPhoneNumberVerificationToken();
            repo.save(user);
            throw new TokenExpiredException();
        }

        user.getAuth().setEnabled(true);
        user.getAuth().setDeleted(false);
        user.getAuth().resetPhoneNumberVerificationToken();
        user.getAuth().setPhoneNumberVerified(true);
        repo.save(user);
    }

    public void resendAccountVerification(String username) {
        UserEntity user = findByUsername(username, username).orElseThrow(() -> {
            throw new UsernameNotFoundException("user not found");
        });
        if (user.getAuth().isLocked())
            throw new UserLockedException(user.getAuth().getLockedFor());
        if (user.getAuth().isEnabled())
            throw new UserEnabledException();

        if (Checker.isValidEmail(username)) {
            sendEmailVerification(user);
            return;
        }
        if (Checker.isValidPhone(username, "SY")) {
            sendPhoneNumberVerification(user);
            return;
        }

        throw new InvalidUsernameException();

    }

    public String login(LoginReqDTO req) {
        UserEntity user = findByUsername(req.email(), req.phoneNumber()).orElseThrow(() -> {
            throw new UsernameNotFoundException("user not found");
        });
        if (user.getAuth().isDeleted() || !user.getAuth().isEnabled())
            throw new UserNotActiveException();
        if (user.getAuth().isLocked())
            throw new UserLockedException(user.getAuth().getLockedFor());

        if (encoder.matches(req.password(), user.getAuth().getPassword())) {
            user.getAuth().IncRefreshTokenVersion();
            repo.save(user);

            Claims payload = Jwts.claims()
                    .add("type", "refresh")
                    .add("version", user.getAuth().getRefreshTokenVersion())
                    .build();
            return jwt.sign(payload, user.getId().toString(), provider.refreshTokenDuration());
        }

        user.getAuth().IncLoginAttempts();
        if (user.getAuth().getLoginAttempts() > provider.loginAttempts()) {
            user.getAuth().lock(provider.loginLockMessage(), provider.loginLockDuration());
            scheduler.schedule(() -> {
                user.getAuth().unLock();
                repo.save(user);
            }, Instant.now().plusMillis(provider.loginLockDuration()));
        }
        repo.save(user);
        throw new IncorrectPasswordException();

    }

    public String getAccessToken(String refreshToken) {
        UserEntity user = verifyRefreshToken(refreshToken);

        user.getAuth().IncAccessTokenVersion();
        repo.save(user);
        Claims claims = Jwts.claims()
                .add("type", "access")
                .add("version", user.getAuth().getAccessTokenVersion())
                .build();
        return jwt.sign(claims, user.getId().toString(), provider.accessTokenDuration());
    }

    public void logout(String token) {
        UserEntity user = verifyRefreshToken(token);
        user.getAuth().IncAccessTokenVersion();
        user.getAuth().IncRefreshTokenVersion();
        repo.save(user);
    }

    private UserEntity verifyRefreshToken(String token) {
        Claims payload = jwt.verify(token);
        if (!payload.get("type").equals("refresh"))
            throw new InvalidTokenException();

        UserEntity user = repo.findById(Long.parseLong(payload.getSubject())).orElseThrow(() -> {
            throw new UsernameNotFoundException("user not found");
        });

        if (user.getAuth().isLocked())
            throw new UserLockedException(user.getAuth().getLockedFor());

        if (payload.get("version") != user.getAuth().getRefreshTokenVersion())
            throw new InvalidTokenException();

        return user;
    }

    public void resendAccountVerification(UsernameDTO username) {
        Optional<UserEntity> opUser = findByUsername(username.email(), username.phoneNumber());
        if (opUser.isEmpty())
            throw new UsernameNotFoundException("user not found");
        if (opUser.get().getAuth().isEnabled())
            throw new UserEnabledException();

        if (Checker.isValidEmail(username.email())) {
            sendEmailVerification(opUser.get());
            return;
        }
        if (Checker.isValidPhone(username.phoneNumber(), "SY")) {
            sendPhoneNumberVerification(opUser.get());
            return;
        }

        throw new InvalidUsernameException();
    }

}