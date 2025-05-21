package com.drmtaxi.drm_taxi.Services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.drmtaxi.drm_taxi.Configs.EnvironmentChecker;
import com.drmtaxi.drm_taxi.Configs.PropertiesProvider;
import com.drmtaxi.drm_taxi.DTOs.UserInfoResponseDTO;
import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.BadInputsException;
import com.drmtaxi.drm_taxi.Repositories.UserRepo;
import com.drmtaxi.drm_taxi.Utils.Messager;
import com.drmtaxi.drm_taxi.Utils.PhoneNumberChecker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepo userRepo;
    private final EmailService mailer;
    private final EnvironmentChecker env;
    private final JwtService jwt;
    private final PropertiesProvider provider;
    private final PasswordEncoder encoder;


    public Optional<UserEntity> getByUsername(String username){
        if(username == null) return null;
        if(username.contains("@")) return userRepo.findByEmail(username);
        if(PhoneNumberChecker.isValidPhone(username, "SY")) return userRepo.findByPhoneNumber(username);
        return Optional.empty();
    }


    private void sendAccountVerificationToken(UserEntity user){
        String token = "";

        user.getAuth().setPhoneVerificationToken(null);
        user.getAuth().setPhoneVerificationTokenExpiresAt(null);
        user.getAuth().setPhoneVerificationTokenInitiatedAt(null);
        user.getAuth().setEmailVerificationToken(null);
        user.getAuth().setEmailVerificationTokenExpiresAt(null);
        user.getAuth().setEmailVerificationTokenInitiatedAt(null);


        if(user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()){
            SecureRandom secureRandom = new SecureRandom();
            String code = 100000 + secureRandom.nextInt(900000) + "";
        
            user.getAuth().setPhoneVerificationToken(code);
            user.getAuth().setPhoneVerificationTokenExpiresAt(LocalDateTime.now().plusSeconds(verifyDuration() / 1000));
            user.getAuth().setPhoneVerificationTokenInitiatedAt(LocalDateTime.now());

            if(env.isDevelopment()){
                System.out.println("==========================================================================");
                System.out.println("code = " + code);
                System.out.println("==========================================================================");
            }

        }
        else if(user.getEmail() != null && !user.getEmail().isEmpty())
            try {
                String rand = UUID.randomUUID().toString();
                ClaimsBuilder claims = Jwts.claims().add("rand", rand)
                                                    .add("type", "email")
                                                    .add("username", user.getEmail());
                token = jwt.sign(claims.build(), user.getId().toString(), verifyDuration());
                
                user.getAuth().setEmailVerificationToken(rand);
                user.getAuth().setEmailVerificationTokenExpiresAt(LocalDateTime.now().plusSeconds(verifyDuration() / 1000));
                user.getAuth().setEmailVerificationTokenInitiatedAt(LocalDateTime.now());
    
                if(env.isDevelopment()){
                    System.out.println("token = " + token);
                    System.out.println("rand = " + rand);
                }
                mailer.sendVerifyToken(user.getEmail(), token, user.getId());
            } catch (MessagingException e) {
                throw new RuntimeException(env.isDevelopment()? e.getMessage() : "An unexpected error occurred");
            }

        userRepo.save(user);
    }

    private void sendForgetPasswordToken(UserEntity user){
        String token = "";

        user.getAuth().setForgetPasswordToken(null);
        user.getAuth().setForgetPasswordTokenExpiresAt(null);


        if(user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()){
            SecureRandom secureRandom = new SecureRandom();
            String code = 100000 + secureRandom.nextInt(900000) + "";
        
            user.getAuth().setForgetPasswordToken(code);
            user.getAuth().setForgetPasswordTokenExpiresAt(LocalDateTime.now().plusSeconds(verifyDuration() / 1000));

            if(env.isDevelopment()){
                System.out.println("==========================================================================");
                System.out.println("code = " + code);
                System.out.println("==========================================================================");
            }

        }
        else if(user.getEmail() != null && !user.getEmail().isEmpty())
            try {
                String rand = UUID.randomUUID().toString();
                ClaimsBuilder claims = Jwts.claims().add("rand", rand)
                                                    .add("type", "email")
                                                    .add("username", user.getEmail());
                token = jwt.sign(claims.build(), user.getId().toString(), verifyDuration());
                
                user.getAuth().setForgetPasswordToken(rand);
                user.getAuth().setForgetPasswordTokenExpiresAt(LocalDateTime.now().plusSeconds(verifyDuration() / 1000));

                if(env.isDevelopment()){
                    System.out.println("==========================================================================");
                    System.out.println("token = " + token);
                    System.out.println("==========================================================================");
                }
                mailer.sendResetPasswordToken(user.getEmail(), token, user.getId());
            } catch (MessagingException e) {
                throw new RuntimeException(env.isDevelopment()? e.getMessage() : "An unexpected error occurred");
            }

        userRepo.save(user);
    }

    @Transactional   
    public UserInfoResponseDTO basicSignup(UserEntity newUser) {
        if(newUser.getPhoneNumber() != null && newUser.getPhoneNumber().startsWith("09")) newUser.setPhoneNumber(newUser.getPhoneNumber().replace("09", "+963"));
        Optional<UserEntity> usernameExists = Optional.empty();
        if(newUser.getEmail() != null && newUser.getEmail().contains("@")) usernameExists = userRepo.findByEmail(newUser.getEmail());
        if(newUser.getPhoneNumber() != null && PhoneNumberChecker.isValidPhone(newUser.getPhoneNumber(), "SY")) usernameExists = userRepo.findByPhoneNumber(newUser.getPhoneNumber());
        
        if(usernameExists.isPresent()) throw new RuntimeException("Username already exists");
        newUser.getAuth().setPassword(encoder.encode(newUser.getAuth().getPassword()));
        UserEntity user = userRepo.save(newUser);
        sendAccountVerificationToken(user);
        return new UserInfoResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getBirthDate(), user.getAuth().getRoles());
    }

    private int verifyDuration(){
        return provider.verifyDuration() != 0? provider.verifyDuration(): 15 * 60 * 1000;
    }

    @Transactional
    public void verifyEmail(String token, Long id) {
        Claims payload = jwt.verify(token);
        if(Long.valueOf(payload.getSubject()) != id) throw new JwtException("invalid token or id mismatch");
        Optional<UserEntity> opUser = userRepo.findById(id);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(payload.get("username").toString()));
        UserEntity user = opUser.get();
        if(user.getAuth().getEnabled()) throw new BadInputsException(Messager.userEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());

        if(payload.get("type").equals("email")){
            if(
                payload.get("rand").equals(user.getAuth().getEmailVerificationToken()) &&
                user.getAuth().getEmailVerificationTokenExpiresAt().isAfter(LocalDateTime.now())
            ){
                user.getAuth().setEnabled(true);
                user.getAuth().setEmailVerificationToken(null);
                user.getAuth().setEmailVerificationTokenExpiresAt(null);
                userRepo.save(user);
            }else throw new BadInputsException(Messager.invalidToken());
        }
        
    }

    @Transactional
    public void verifyPhoneNumber(String code, Long id){
        Optional<UserEntity> opUser = userRepo.findById(id);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(""));
        UserEntity user = opUser.get();
        if(user.getAuth().getEnabled()) throw new BadInputsException(Messager.userEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());

        if(
        code.equals(user.getAuth().getPhoneVerificationToken()) &&
        user.getAuth().getPhoneVerificationTokenExpiresAt().isAfter(LocalDateTime.now())
        ){
            user.getAuth().setEnabled(true);
            user.getAuth().setPhoneVerificationToken(null);
            user.getAuth().setPhoneVerificationTokenExpiresAt(null);
            userRepo.save(user);
        }else throw new BadInputsException(Messager.invalidToken());
        
    }

    @Transactional
    public void resendVerificationAccount(String username) {
        Optional<UserEntity> opUser = getByUsername(username);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(username));
        UserEntity user = opUser.get();
        if(user.getAuth().getEnabled()) throw new BadInputsException(Messager.userEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());
        sendAccountVerificationToken(user);    
    }

    @Transactional
    public void sendForgetPassword(String username) {
        Optional<UserEntity> opUser = getByUsername(username);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(username));
        UserEntity user = opUser.get();
        if(!user.getAuth().getEnabled()) throw new BadInputsException(Messager.userNotEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());
        sendForgetPasswordToken(user);
    }

    @Transactional
    public void verifyAndResetPasswordPhone(String code, Long id, String password) {
        Optional<UserEntity> opUser = userRepo.findById(id);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(""));
        UserEntity user = opUser.get();
        if(!user.getAuth().getEnabled()) throw new BadInputsException(Messager.userNotEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());

        if(
            code.equals(user.getAuth().getForgetPasswordToken()) &&
            user.getAuth().getForgetPasswordTokenExpiresAt().isAfter(LocalDateTime.now())
            ){
                user.getAuth().setPassword(encoder.encode(password));;
                user.getAuth().setForgetPasswordToken(null);
                user.getAuth().setForgetPasswordTokenExpiresAt(null);
                userRepo.save(user);
            }else throw new BadInputsException(Messager.invalidToken());
            
    }

    public void verifyAndResetPasswordEmail(String token, Long id, String password) { 
        Claims payload = jwt.verify(token);
        if(Long.valueOf(payload.getSubject()) != id) throw new JwtException("invalid token or id mismatch");
        Optional<UserEntity> opUser = userRepo.findById(id);
        if(opUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(payload.get("username").toString()));
        UserEntity user = opUser.get();
        if(!user.getAuth().getEnabled()) throw new BadInputsException(Messager.userNotEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());

        if(payload.get("type").equals("email")){
            if(
                payload.get("rand").equals(user.getAuth().getForgetPasswordToken()) &&
                user.getAuth().getForgetPasswordTokenExpiresAt().isAfter(LocalDateTime.now())
            ){
                user.getAuth().setPassword(encoder.encode(password));
                user.getAuth().setForgetPasswordToken(null);
                user.getAuth().setForgetPasswordTokenExpiresAt(null);
                userRepo.save(user);
            }else throw new BadInputsException(Messager.invalidToken());
        }
        
       }



    //testing purposes
    public String usernamePasswordVerify(String username, String password) {
        Optional<UserEntity> optionalUser = getByUsername(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(Messager.userNotFound(username));
        UserEntity user = optionalUser.get();
        if(!user.getAuth().getEnabled()) throw new BadInputsException(Messager.userNotEnabled());
        if(user.getAuth().getLocked()) throw new BadInputsException(Messager.userLocked());
        if(user.getAuth().getCredintialsExpired()) throw new BadInputsException(Messager.userCredintialsExpired());

        return jwt.sign(user.getEmail(), 15 * 60 * 1000);
    }


}
