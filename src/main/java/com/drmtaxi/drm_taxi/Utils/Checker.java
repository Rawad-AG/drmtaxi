package com.drmtaxi.drm_taxi.Utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.regex.Pattern;

import com.drmtaxi.drm_taxi.Exceptions.exceptions.auth.InvalidPhoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;

public class Checker {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phoneNumber, String region) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, region);

            return phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String parsePhoneNumber(String number) {
        if (number == null)
            return null;
        number = number.trim();
        if (number.startsWith("+963"))
            return number.replace("+", "");
        if (number.startsWith("963"))
            return number;
        if (number.startsWith("09"))
            return number.replace("09", "9639");
        throw new InvalidPhoneNumberException();

    }
}
