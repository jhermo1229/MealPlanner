package com.example.mealplanner;

import androidx.annotation.NonNull;

/**
 * Main model for the user.
 */
public class User {

    private String email;
    private String password;
    private UserDetails userDetails;
    private UserMealDetails userMealDetails;
    private UserPaymentDetails userPaymentDetails;


    public String getPassword() {
        byte[] decodedBytes = android.util.Base64.decode(password, android.util.Base64.DEFAULT);
        return new String(decodedBytes);
    }


    public void setPassword(@NonNull String password) {
        this.password = android.util.Base64.encodeToString(password.getBytes(), android.util.Base64.DEFAULT);
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserMealDetails getUserMealDetails() {
        return userMealDetails;
    }

    public void setUserMealDetails(UserMealDetails userMealDetails) {
        this.userMealDetails = userMealDetails;
    }

    public UserPaymentDetails getUserPaymentDetails() {
        return userPaymentDetails;
    }

    public void setUserPaymentDetails(UserPaymentDetails userPaymentDetails) {
        this.userPaymentDetails = userPaymentDetails;
    }
}
