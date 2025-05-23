package com.drmtaxi.drm_taxi.Services;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drmtaxi.drm_taxi.Configs.PropertiesProvider;
import com.drmtaxi.drm_taxi.DTOs.LoginDTO;
import com.drmtaxi.drm_taxi.DTOs.SignupDriverDTO;
import com.drmtaxi.drm_taxi.DTOs.SignupUserDTO;
import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.BadInputsException;
import com.drmtaxi.drm_taxi.Repositories.UserRepo;
import com.drmtaxi.drm_taxi.Security.AppUser;
import com.drmtaxi.drm_taxi.Utils.Messager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;

import com.drmtaxi.drm_taxi.Utils.Checker;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final EmailService mailer;
    private final PropertiesProvider provider;
    private final JwtService jwt;
    private final PasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException(Messager.userNotFound(username));
        }
        if (Checker.isValidEmail(username)) {
            Optional<UserEntity> user = repo.findByEmail(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException(Messager.userNotFound(username));
            }
            return new AppUser(user.get());
        }
        if (Checker.isValidPhone(username, "SY")) {
            Optional<UserEntity> user = repo.findByPhoneNumber(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException(Messager.userNotFound(username));
            }
            return new AppUser(user.get());
        }
        throw new UsernameNotFoundException(Messager.userNotFound(username));
    }

    @Transactional
    private void sendVerificationEmail(UserEntity user) {
        String uuid = UUID.randomUUID().toString();
        user.getAuth().setAccountVerificationUUID(uuid, provider.verifyDuration());
        repo.save(user);
        Claims claims = Jwts.claims().add("uuid", uuid).build();
        String token = jwt.sign(claims, user.getId().toString(), provider.verifyDuration());
        try {
            mailer.sendVerifyToken(user.getEmail(), token, user.getId());
        } catch (MessagingException e) {
            user.getAuth().setAccountVerificationUUID(null);
            user.getAuth().setAccountVerificationUUIDExpiresAt(null);
            repo.save(user);
        }

    }

    @Transactional
    private void sendVerificationCode(UserEntity user) {
        Random random = new Random();
        Integer token = 100000 + random.nextInt(900000);
        user.getAuth().setAccountVerificationUUID(token.toString(), provider.verifyDuration());
        repo.save(user);
        try {
            mailer.sendVerifyToken(user.getPhoneNumber(), token.toString(), user.getId());
        } catch (MessagingException e) {
            user.getAuth().setAccountVerificationUUID(null);
            user.getAuth().setAccountVerificationUUIDExpiresAt(null);
            repo.save(user);
        }
    }

    @Transactional
    public void signupUser(SignupUserDTO userDTO) {
        if (Checker.isValidEmail(userDTO.username())) {
            Optional<UserEntity> opUser = repo.findByEmail(userDTO.username());
            if (opUser.isPresent()) {
                UserEntity user = opUser.get();
                if (user.isDriver())
                    throw new BadInputsException(Messager.DriverAttempsToUserSignup());
                if (user.isDeleted() || !user.getAuth().isEnable()) {
                    sendVerificationEmail(user);
                    return;
                }
                throw new BadInputsException(Messager.userEnabled());
            }
            UserEntity user = repo.save(new UserEntity(userDTO.firstName(), userDTO.lastName(), userDTO.gender(),
                    userDTO.username(), null, encoder.encode(userDTO.password())));
            sendVerificationEmail(user);
            return;
        }
        if (Checker.isValidPhone(userDTO.username(), "SY")) {
            Optional<UserEntity> opUser = repo
                    .findByPhoneNumber(Checker.parsePhoneNumber(userDTO.username()));
            if (opUser.isPresent()) {
                UserEntity user = opUser.get();
                if (user.isDeleted() || !user.getAuth().isEnable()) {
                    sendVerificationCode(user);
                }
                throw new BadInputsException(Messager.userEnabled());
            }
            UserEntity user = repo.save(new UserEntity(userDTO.firstName(), userDTO.lastName(), userDTO.gender(),
                    null, Checker.parsePhoneNumber(userDTO.username()), encoder.encode(userDTO.password())));
            sendVerificationCode(user);
            return;
        }
        throw new BadInputsException(Messager.incorrectUsername(userDTO.username()));
    }

    @Transactional
    public void signupDriver(SignupDriverDTO driverDTO) {
        if (Checker.isValidPhone(driverDTO.phoneNumber(), "SY")) {
            Optional<UserEntity> opUser = repo.findByPhoneNumber(Checker.parsePhoneNumber(driverDTO.phoneNumber()));
            if (opUser.isPresent()) {
                UserEntity user = opUser.get();
                if (!user.isDriver())
                    throw new BadInputsException(Messager.UserAttempsToDriverSignup());
                if (user.isDeleted() || !user.getAuth().isEnable()) {
                    sendVerificationCode(user);
                }
                throw new BadInputsException(Messager.userEnabled());
            }
            UserEntity user = repo.save(new UserEntity(driverDTO.firstName(), driverDTO.lastName(), driverDTO.gender(),
                    Checker.parsePhoneNumber(driverDTO.phoneNumber()), encoder.encode(driverDTO.password()),
                    driverDTO.licenseNumber(),
                    driverDTO.carCompany(),
                    driverDTO.carModel(), driverDTO.carNumber(), driverDTO.carColor(), driverDTO.idNumber(),
                    driverDTO.livingLocation(), driverDTO.fatherName(), driverDTO.dateOfBirth(),
                    driverDTO.locationOfBirth(), driverDTO.startWorkHour(), driverDTO.endWorkHour()));
            sendVerificationCode(user);
            return;
        }
        throw new BadInputsException(Messager.incorrectUsername(driverDTO.phoneNumber()));
    }

    @Transactional
    public void verifyAccound(String token, String username) {
        if (token.length() == 6 && username != null && username.length() > 0) {
            Optional<UserEntity> opUser = repo.findByPhoneNumber(Checker.parsePhoneNumber(username));
            if (opUser.isEmpty())
                throw new UsernameNotFoundException(Messager.userNotFound(username));
            UserEntity user = opUser.get();
            if (user.getAuth().getAccountVerificationUUID() == null)
                throw new UsernameNotFoundException(Messager.userNotFound(username));
            if (!user.getAuth().getAccountVerificationUUID().equals(token))
                throw new UsernameNotFoundException(Messager.userNotFound(username));
            if (user.getAuth().getAccountVerificationUUIDExpiresAt().isBefore(Instant.now()))
                throw new UsernameNotFoundException(Messager.userCredintialsExpired());
            user.getAuth().setEnable(true);
            user.setDeleted(false);
            user.getAuth().setAccountVerificationUUID(null);
            user.getAuth().setAccountVerificationUUIDExpiresAt(null);
            repo.save(user);
            return;
        }

        Claims payload = jwt.verify(token);
        Optional<UserEntity> opUser = repo.findById(Long.parseLong(payload.getSubject()));
        if (opUser.isEmpty())
            throw new UsernameNotFoundException(Messager.userNotFound(""));
        UserEntity user = opUser.get();
        if (user.getAuth().getAccountVerificationUUID() == null)
            throw new UsernameNotFoundException(Messager.userNotFound(""));
        if (!user.getAuth().getAccountVerificationUUID().equals(payload.get("uuid")))
            throw new UsernameNotFoundException(Messager.userNotFound(""));
        if (user.getAuth().getAccountVerificationUUIDExpiresAt().isBefore(Instant.now()))
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        user.getAuth().setEnable(true);
        user.setDeleted(false);
        user.getAuth().setAccountVerificationUUID(null);
        user.getAuth().setAccountVerificationUUIDExpiresAt(null);
        repo.save(user);

    }

    public String login(LoginDTO credintials) {
        Optional<UserEntity> opUser = Optional.empty();
        if (Checker.isValidEmail(credintials.username()))
            opUser = repo.findByEmail(credintials.username());
        if (Checker.isValidPhone(credintials.username(), "SY"))
            opUser = repo.findByPhoneNumber(credintials.username());
        if (opUser.isEmpty())
            throw new UsernameNotFoundException(Messager.userNotFound(credintials.username()));
        UserEntity user = opUser.get();
        if (!user.getAuth().isEnable() || user.isDeleted())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (user.isDriver() && !user.getDriverInfo().isInWork())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (encoder.matches(credintials.password(), user.getAuth().getPassword())) {
            Claims payload = Jwts.claims().add("version", user.getAuth().getTokensVersion()).build();
            return jwt.sign(payload, user.getUsername(), provider.refreshTokenDuration());
        }
        throw new BadInputsException(Messager.incorrectPassword());
    }

    public String getAccessToken(String refreshToken) {
        Optional<UserEntity> opUser = Optional.empty();
        Claims payload = jwt.verify(refreshToken);

        if (Checker.isValidEmail(payload.getSubject()))
            opUser = repo.findByEmail(payload.getSubject());
        if (Checker.isValidPhone(payload.getSubject(), "SY"))
            opUser = repo.findByPhoneNumber(payload.getSubject());
        if (opUser.isEmpty())
            throw new UsernameNotFoundException(Messager.userNotFound(payload.getSubject()));
        UserEntity user = opUser.get();
        if (!user.getAuth().isEnable() || user.isDeleted())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (user.isDriver() && !user.getDriverInfo().isInWork())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (user.getAuth().getTokensVersion() != (double) payload.get("version"))
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        Claims claims = Jwts.claims().add("version", user.getAuth().getTokensVersion()).build();
        return jwt.sign(claims, user.getUsername(), provider.accessTokenDuration());
    }

    public void logout(String refreshToken) {
        Optional<UserEntity> opUser = Optional.empty();
        Claims payload = jwt.verify(refreshToken);

        if (Checker.isValidEmail(payload.getSubject()))
            opUser = repo.findByEmail(payload.getSubject());
        if (Checker.isValidPhone(payload.getSubject(), "SY"))
            opUser = repo.findByPhoneNumber(payload.getSubject());
        if (opUser.isEmpty())
            throw new UsernameNotFoundException(Messager.userNotFound(payload.getSubject()));
        UserEntity user = opUser.get();
        if (!user.getAuth().isEnable() || user.isDeleted())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (user.isDriver() && !user.getDriverInfo().isInWork())
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        if (user.getAuth().getTokensVersion() != (double) payload.get("version"))
            throw new UsernameNotFoundException(Messager.userCredintialsExpired());
        user.getAuth().setTokensVersion(user.getAuth().getTokensVersion() + 1);
        repo.save(user);
    }

}