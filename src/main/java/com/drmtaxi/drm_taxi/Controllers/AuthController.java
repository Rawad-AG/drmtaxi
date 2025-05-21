package com.drmtaxi.drm_taxi.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drmtaxi.drm_taxi.Configs.EnvironmentChecker;
import com.drmtaxi.drm_taxi.DTOs.ForgetPasswordDTO;
import com.drmtaxi.drm_taxi.DTOs.SuccessResponse;
import com.drmtaxi.drm_taxi.DTOs.UserInfoDTO;
import com.drmtaxi.drm_taxi.DTOs.UserInfoResponseDTO;
import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.BadInputsException;
import com.drmtaxi.drm_taxi.Exceptions.exceptions.RouteNotFound;
import com.drmtaxi.drm_taxi.Services.AuthService;
import com.drmtaxi.drm_taxi.Utils.Messager;
import com.drmtaxi.drm_taxi.Utils.PhoneNumberChecker;
import com.drmtaxi.drm_taxi.Utils.UserRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@AllArgsConstructor
@RestController
@RequestMapping("/web/v1/auth")
public class AuthController {
    private final AuthService service;
    private final EnvironmentChecker env;
    

     @GetMapping("/csrf")
    public ResponseEntity<SuccessResponse> getCSRFToken(HttpServletRequest request) {
        if(env.isProduction()) throw new RouteNotFound(Messager.routeNotFound("/web/v1/auth/csrf"));
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("this route is for development purposes, it will be removed in the production", token.getToken()));
    }


    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signupUsernamePassword(@Valid @RequestBody UserInfoDTO user){
        ArrayList<UserRoles> roles = new ArrayList<>();
        roles.add(UserRoles.USER);
        UserInfoResponseDTO userRes;
        if(user.username().contains("@"))
            userRes = service.basicSignup(new UserEntity(user.username(), null,user.password(), user.firstName(), user.LastName(),user.birthDate(), roles));
        else if(PhoneNumberChecker.isValidPhone(user.username(), "SY"))
            userRes = service.basicSignup(new UserEntity(null, user.username(), user.password(), user.firstName(), user.LastName(),user.birthDate(), roles));
        else throw new BadInputsException(Messager.incorrectUsername(user.username()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(Messager.signupSuccessfully(user.username()) , userRes));
    } 


    @GetMapping("/verifyaccount")
    public ResponseEntity<SuccessResponse> verifyAccount(@RequestParam("token") String token, @RequestParam("id") Long id){
        if(token.length() == 6) service.verifyPhoneNumber(token, id); 
        else service.verifyEmail(token, id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(Messager.emailVerified()));
    } 

    @GetMapping("/verifyaccount/resend")
    public ResponseEntity<SuccessResponse> resendVerificationAccount(@RequestParam("username") String username){
        service.resendVerificationAccount(username);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(Messager.resendVerificationAccount(username)));
    } 

    @GetMapping("/forgetpassword")
    public ResponseEntity<SuccessResponse> forgetPassword(@RequestParam("username") String username){
        service.sendForgetPassword(username);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(Messager.sendForgetPassword(username)));
    } 
    
    
    @PostMapping("/forgetpassword/verify")
    public ResponseEntity<SuccessResponse> verifyForgetPassword(@Valid @RequestBody ForgetPasswordDTO forgetPass){
        if(forgetPass.token().length() == 6) service.verifyAndResetPasswordPhone(forgetPass.token(), forgetPass.id(), forgetPass.password()); 
        else service.verifyAndResetPasswordEmail(forgetPass.token(), forgetPass.id(), forgetPass.password());
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(Messager.passwordForgetVerified()));
    } 


    public ResponseEntity<SuccessResponse> changePassword(){
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    } 



    public ResponseEntity<SuccessResponse> updateInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(""));
    } 
    
}
