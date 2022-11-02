package com.example.mealplanner;

public class User {

    private String firstName;
    private String lastName;
    private String houseNumber;
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String password;
    private Integer deliveryFrequency;
    private String mealType;
    private Integer mealQuantity;


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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(Integer deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(Integer mealQuantity) {
        this.mealQuantity = mealQuantity;
    }
}
