package com.foodies.mealplanner;

/**
 * Main model for the user.
 */
public class User {

    private String email;
    private String password;
    private UserDetails userDetails;
    private UserMealDetails userMealDetails;
    private UserPaymentDetails userPaymentDetails;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
