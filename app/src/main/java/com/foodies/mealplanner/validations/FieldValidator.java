package com.foodies.mealplanner.validations;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class FieldValidator {

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

    /**
     * Check all required fields if value is present
     *
     * @return boolean, true if all valid
     */
    public boolean validateFieldIfEmpty(TextInputLayout inputLayout) {
        if (inputLayout.getEditText().length() == 0) {
            inputLayout.setError("Required");
            return true;
        } else {
            inputLayout.setError(null);
        }
        return false;
    }
}
