package com.foodies.mealplanner;

/**
 * Model class for payment details of user.
 */
public class UserPaymentDetails {

    private Double cardNumber;
    private Integer expiryDate;
    private String securityCode;
    private String nameOnCard;

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Double cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }
}
