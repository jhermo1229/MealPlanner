package com.foodies.mealplanner.validations;

import java.util.regex.Pattern;

public class PersonalDetailsValidator {

    /**
     * Checker for email format
     *
     * @param email
     * @return true if matches
     */
    public boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
