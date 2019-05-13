package com.example.lab1.model;

import java.io.Serializable;

public class PendingRequestAdapterModel implements Serializable {
    private String restaurantName;
    private String restaurantAddress;
    private float restaurantDistance;
    private String restaurantPhoneNumber;

    private String paymentType; // can be "Cash" or "Online"
    private float price;

    private String clientName;
    private String clientAddress;
    private float clientDistance;
    private String clientPhoneNumber;
    private int orderNumber;

    private Boolean accept;
    private Boolean reject;

    public PendingRequestAdapterModel(String restaurantName, String restaurantAddress, String restaurantPhoneNumber, String clientName, String clientAddress, String clientPhoneNumber, String paymentType, float price, int orderNumber) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantDistance = 0; //should be computed btw the rider and the restaurant positions
        this.restaurantPhoneNumber = restaurantPhoneNumber;

        this.paymentType = paymentType;
        this.price = price; //should be equal to 0 if paymentType.equals("online")

        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientDistance = 0; //should be computed btw the rider and the client positions
        this.clientPhoneNumber = clientPhoneNumber;
        this.orderNumber = orderNumber;

        this.accept = false;
        this.reject = false;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public float getRestaurantDistance() {
        return restaurantDistance;
    }

    public void setRestaurantDistance(float restaurantDistance) {
        this.restaurantDistance = restaurantDistance;
    }

    public String getRestaurantPhoneNumber() {
        return restaurantPhoneNumber;
    }

    public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
        this.restaurantPhoneNumber = restaurantPhoneNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public float getClientDistance() {
        return clientDistance;
    }

    public void setClientDistance(float clientDistance) {
        this.clientDistance = clientDistance;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public Boolean getReject() {
        return reject;
    }

    public void setReject(Boolean reject) {
        this.reject = reject;
    }

}
