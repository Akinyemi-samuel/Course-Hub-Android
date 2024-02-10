package com.example.coursehub.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean isEmailValid(String e) {
        // All of my validations

        if (e.length() <= 2) {
            return false;
        }

        if (!regEx(e)) {
            return false;
        }

        return true;
    }


    private static boolean regEx(String text) {

        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean result = m.matches();
        return result;

    }

}
