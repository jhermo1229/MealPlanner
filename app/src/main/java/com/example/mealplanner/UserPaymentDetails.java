package com.example.mealplanner;

/**
 * Model class for payment details of user.
 */
public class UserPaymentDetails {

    private Integer cardNumber;
    private Integer expiryDate;
    private Integer securityCode;
    private Address address;

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
