package com.example.lab1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// to update !!

public class ProfileInfo implements Serializable {
    private String bikerID;
    private String username;
    private String firstName;
    private String lastName;
    private String phone_nb;
    private String email_address;
    private String description;
    private String codice_fiscale;
    private String profile_picture_uri;
    private boolean already_filled;
    private boolean isAvailable;
    private String latitude;
    private String longitude;
    private Float starsRating;
    private Float kmCounter;
    private Float moneyCounter;
    private Boolean biker_completed = false;

    public ProfileInfo(){ }

    public String getCodiceFiscale() {
        return codice_fiscale;
    }

    public void setCodiceFiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateUsername();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateUsername();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Float getStarsRating() {
        return starsRating;
    }

    public void setStarsRating(Float starsRating) {
        this.starsRating = starsRating;
    }

    private void updateUsername(){
        this.username = this.firstName + " " + this.lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setPhone_nb(String phone_nb) {
        this.phone_nb = phone_nb;
    }

    public String getPhone_nb() {
        return phone_nb;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setProfile_picture_uri(String profile_picture_uri) {
        this.profile_picture_uri = profile_picture_uri;
    }

    public String getProfile_picture_uri() {
        return profile_picture_uri;
    }

    public void setAlready_filled(boolean already_filled) {
        this.already_filled = already_filled;
    }

    public boolean isAlready_filled() {
        return already_filled;
    }

    public Float getKmCounter() {
        return kmCounter;
    }

    public void setKmCounter(Float kmCounter) {
        this.kmCounter = kmCounter;
    }

    public Float getMoneyCounter() {
        return moneyCounter;
    }

    public void setMoneyCounter(Float moneyCounter) {
        this.moneyCounter = moneyCounter;
    }

    public String getBikerID() {
        return bikerID;
    }

    public void setBikerID(String bikerID) {
        this.bikerID = bikerID;
    }

    public Boolean getBiker_completed() {
        return biker_completed;
    }

    public void setBiker_completed(Boolean biker_completed) {
        this.biker_completed = biker_completed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ProfileInfo(ArrayList<String> arrayList){
        this.firstName = arrayList.get(0);
        this.lastName = arrayList.get(1);
        this.username = firstName + " " + lastName;
        this.phone_nb = arrayList.get(2);
        this.email_address = arrayList.get(3);
        this.codice_fiscale = arrayList.get(4);
        this.description = arrayList.get(5);
        this.profile_picture_uri = arrayList.get(6);
        this.isAvailable = Boolean.parseBoolean(arrayList.get(7));
        this.latitude = arrayList.get(8);
        this.longitude = arrayList.get(9);
        this.already_filled = true;
        this.biker_completed = true;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> al = new ArrayList<String>();
        al.add(this.firstName);
        al.add(this.lastName);
        al.add(this.phone_nb);
        al.add(this.email_address);
        al.add(this.codice_fiscale);
        al.add(this.description);
        al.add(this.profile_picture_uri);
        al.add(String.valueOf(this.isAvailable));
        al.add(this.latitude);
        al.add(this.longitude);
        return al;
    }
}

