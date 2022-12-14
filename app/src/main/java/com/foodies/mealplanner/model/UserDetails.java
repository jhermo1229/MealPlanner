package com.foodies.mealplanner.model;

import java.io.Serializable;

/**
 * Model for user details.
 *
 * @author herje
 * @version 1
 */
public class UserDetails implements Serializable {

    private String firstName;
    private String lastName;
    private Address address;
    private String phoneNumber;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
