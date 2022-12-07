package com.foodies.mealplanner.validations;

import java.util.regex.Pattern;

/**
 * Validator for checking for correct email format
 * @author herje
 * @version 1
 */
public class FieldValidator {

    /**
     * Checker for email format
     *
     * @param email - email to be checked.
     * @return true if matches
     */
    public boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (null == email) {
            return false;
        }
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    /**
     * Check all required fields if value is present
     * @param inputLength - 0 if there is no input.
     * @return boolean, true if all valid
     */
    public boolean validateFieldIfEmpty(Integer inputLength) {
        return inputLength == 0;
    }

    /**
     * Check if input is not less than what is required.
     *
     * @param requiredNoInput - required number of input.
     * @param numberOfInput   - input by user.
     * @return boolean
     */
    public boolean validateIfInputIsLess(int requiredNoInput, int numberOfInput) {
        return requiredNoInput > numberOfInput;
    }
}
