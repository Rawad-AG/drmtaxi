package com.drmtaxi.drm_taxi.Utils;

public class Messager {
    public static String userNotFound(String username){
        if(username.contains("@")) return "email " + username + " was not found. Register now";
        if(PhoneNumberChecker.isValidPhone(username, "SY")) return "phone number " + username + " was not found. Register now";
        return "user with " + username + " was not found!";
    }

    public static String routeNotFound(String route) {
        return "route " + route + "was not found!";
    }

    public static String incorrectUsername(String username) {
        if(username.contains("@")) return "email " + username + " is incorrect";
        if (PhoneNumberChecker.isValidPhone(username, "SY")) return "phone number " + username + " is incorrect";
        return "username " + username + " is incorrect";
    }

    public static String invalidToken() {
        return "invalid token";
    }

    public static String userEnabled() {
        return "user is already enabled";
    }
    public static String userNotEnabled() {
        return "user is not enabled";
    }

    public static String userLocked() {
    return "user is blocked";
    }

    public static String userCredintialsExpired() {
        return "user credintials expired";
    }

    public static String signupSuccessfully(String username) {
        if(username.contains("@")) return "signup successfully, check your email to verify your account";
        if (PhoneNumberChecker.isValidPhone(username, "SY")) return "signup successfully, check your sms messages to verify your account";
        return "signup successfully";
    }

    public static String emailVerified() {
        return "email verified successfully";
    }

    public static String phoneVerified() {
        return "phone number verified successfully";
    }

    public static String resendVerificationAccount(String username) {
        if(username.contains("@")) return "we sended an email to " + username + " to verify your account";
        if (PhoneNumberChecker.isValidPhone(username, "SY")) return "we sended an sms to " + username + " to verify your account";
        return "verification otp is send successfully";
    }
    
    public static String sendForgetPassword(String username) {
        if(username.contains("@")) return "we sended an email to " + username + " to reset your password";
           if (PhoneNumberChecker.isValidPhone(username, "SY")) return "we sended an sms to " + username + " to reset your password";
           return "verification otp is send successfully";
       
    }

    public static String passwordForgetVerified() {
        return "new password saved successfully";
    }
}
