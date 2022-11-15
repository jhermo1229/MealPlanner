package com.foodies.mealplanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.foodies.mealplanner.validations.FieldValidator;

import org.junit.Test;

/**
 * Unit testing of personal details fields
 */
public class FieldValidatorTest {

    FieldValidator personalDetailsValidator = new FieldValidator();

    @Test
    public void emailValidator_CorrectSimpleEmail_True() {
        assertTrue(personalDetailsValidator.isValidEmail(("foodies@email.com")));
    }

    @Test
    public void emailValidator_CorrectEmailSubDomain_True() {
        assertTrue(personalDetailsValidator.isValidEmail("foodies@email.co.uk"));
    }

    @Test
    public void emailValidator_InvalidEmailNoTld_False() {
        assertFalse(personalDetailsValidator.isValidEmail("foodies@email"));
    }

    @Test
    public void emailValidator_InvalidEmailDoubleDot_False() {
        assertFalse(personalDetailsValidator.isValidEmail("foodies@email..com"));
    }

    @Test
    public void emailValidator_InvalidEmailNoUsername_False() {
        assertFalse(personalDetailsValidator.isValidEmail("@email.com"));
    }

    @Test
    public void emailValidator_EmptyString_False() {
        assertFalse(personalDetailsValidator.isValidEmail(""));
    }

    @Test
    public void emailValidator_NullEmail_False() {
        assertFalse(personalDetailsValidator.isValidEmail(null));
    }

    @Test
    public void check_Field_If_Empty_False(){
        assertFalse(personalDetailsValidator.validateFieldIfEmpty(1));
    }

    @Test
    public void check_Field_If_Input_Is_Less_True(){
        assertTrue(personalDetailsValidator.validateIfInputIsLess(10, 9));
    }
}
