package com.example.lab1.model;

import java.io.Serializable;

public class PendingRequestAdapterModel implements Serializable {
    private String restaurantName;
    private String restaurantAddress;
    private double restaurantLatitude;
    private double restaurantLongitude;
    private float restaurantDistance;
    private String restaurantPhoneNumber;

    private String paymentType; // can be "Cash" or "Online"
    private float price;

    private String clientName;
    private String clientAddress;
    private double clientLatitude;
    private double clientLongitude;
    private float clientDistance;
    private String clientPhoneNumber;
    private int orderNumber;
    private String order_id;
    private String foodType_id;

    private Boolean accept;
    private Boolean reject;

    public PendingRequestAdapterModel(String restaurantName, String restaurantAddress, double restaurantLatitude, double restaurantLongitude, float restaurantDistance, String restaurantPhoneNumber, String clientName, String clientAddress, double clientLatitude, double clientLongitude, float clientDistance, String clientPhoneNumber, String paymentType, float price, int orderNumber, String order_id, String foodType_id) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantLatitude = restaurantLatitude;
        this.restaurantLongitude = restaurantLongitude;
        this.restaurantDistance = restaurantDistance;
        this.restaurantPhoneNumber = restaurantPhoneNumber;

        this.paymentType = paymentType;
        this.price = price; //should be equal to 0 if paymentType.equals("online")

        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientLatitude = clientLatitude;
        this.clientLongitude = clientLongitude;
        this.clientDistance = clientDistance;
        this.clientPhoneNumber = clientPhoneNumber;
        this.orderNumber = orderNumber;
        this.order_id = order_id;
        this.foodType_id = foodType_id;

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

    public double getRestaurantLatitude() {
        return restaurantLatitude;
    }

    public void setRestaurantLatitude(double restaurantLatitude) {
        this.restaurantLatitude = restaurantLatitude;
    }

    public double getRestaurantLongitude() {
        return restaurantLongitude;
    }

    public void setRestaurantLongitude(double restaurantLongitude) {
        this.restaurantLongitude = restaurantLongitude;
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

    public double getClientLatitude() {
        return clientLatitude;
    }

    public void setClientLatitude(double clientLatitude) {
        this.clientLatitude = clientLatitude;
    }

    public double getClientLongitude() {
        return clientLongitude;
    }

    public void setClientLongitude(double clientLongitude) {
        this.clientLongitude = clientLongitude;
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getFoodType_id() {
        return foodType_id;
    }

    public void setFoodType_id(String foodType_id) {
        this.foodType_id = foodType_id;
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
