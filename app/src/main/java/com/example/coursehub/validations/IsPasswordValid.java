package com.example.coursehub.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsPasswordValid {

    public static String isPasswordValid(String password) {
        // Check if password length is greater than 8
        if (password.length() <= 2) {
            return "Password is too short";
        }

        if (!containsUpperCase(password)) {
            return "Password must contain at least one uppercase letter.";
        }

         return "";

    }



    public static boolean containsUpperCase(String password) {
        // Define a regular expression to match an uppercase letter
        String regex = ".*[A-Z].*";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(password);

        // Check if the password contains an uppercase letter
        return matcher.matches();
    }
}


