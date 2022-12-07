package com.foodies.mealplanner.model;

import java.io.Serializable;

/**
 * Main model for the user.
 *
 * @author herje
 * @version 1
 */
public class User implements Comparable<User>, Serializable {

    private String email;
    private String password;
    private UserDetails userDetails;
    private UserMealDetails userMealDetails;
    private UserPaymentDetails userPaymentDetails;
    private String userType;
    private String status;
    private String imageUrl;


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


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(User user) {
        return 0;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
