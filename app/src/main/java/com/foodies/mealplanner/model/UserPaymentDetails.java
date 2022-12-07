package com.foodies.mealplanner.model;

import java.io.Serializable;

/**
 * Model class for payment details of user.
 *
 * @author herje
 * @version 1
 */
public class UserPaymentDetails implements Serializable {

    private String cardNumber;
    private Integer expiryDate;
    private String securityCode;
    private String nameOnCard;

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
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
