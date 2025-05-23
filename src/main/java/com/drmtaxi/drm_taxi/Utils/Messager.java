package com.drmtaxi.drm_taxi.Utils;

public class Messager {
    public static String userNotFound(String username) {
        if (Checker.isValidEmail(username))
            return "email " + username + " was not found. Register now";
        if (Checker.isValidPhone(username, "SY"))
            return "phone number " + username + " was not found. Register now";
        return "user with " + username + " was not found!";
    }

    public static String routeNotFound(String route) {
        return "route " + route + "was not found!";
    }

    public static String incorrectUsername(String username) {
        if (Checker.isValidEmail(username))
            return "email " + username + " is incorrect";
        if (Checker.isValidPhone(username, "SY"))
            return "phone number " + username + " is incorrect";
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
        if (Checker.isValidEmail(username))
            return "signup successfully, check your email to verify your account";
        if (Checker.isValidPhone(username, "SY"))
            return "signup successfully, check your sms messages to verify your account";
        return "signup successfully";
    }

    public static String emailVerified() {
        return "email verified successfully";
    }

    public static String phoneVerified() {
        return "phone number verified successfully";
    }

    public static String resendVerificationAccount(String username) {
        if (Checker.isValidEmail(username))
            return "we sended an email to " + username + " to verify your account";
        if (Checker.isValidPhone(username, "SY"))
            return "we sended an sms to " + username + " to verify your account";
        return "verification otp is send successfully";
    }

    public static String sendForgetPassword(String username) {
        if (Checker.isValidEmail(username))
            return "we sended an email to " + username + " to reset your password";
        if (Checker.isValidPhone(username, "SY"))
            return "we sended an sms to " + username + " to reset your password";
        return "verification otp is send successfully";

    }

    public static String passwordForgetVerified() {
        return "new password saved successfully";
    }

    public static String DriverAttempsToUserSignup() {
        return "you already have a driver account, you cannot create a normal user account";
    }

    public static String UserAttempsToDriverSignup() {
        return "you already have a User account, if you want to apply as a driver please submit a driver application";
    }

    public static String incorrectPassword() {
        return "incorrect password";
    }

    public static String logout() {
        return "bye";
    }
}
