package com.example.lab1.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileInfo implements Serializable {
    private String username;
    private String firstName;
    private String lastName;
    private String phone_nb;
    private String email_address;
    private String description;
    private String identity_document;
    private String profile_picture_uri;
    private String background_picture_uri;
    private boolean already_filled;
    private boolean isFree;
    private double[] current_position = new double[2];
    private Float starsRating;
    private Float kmCounter;
    private Float moneyCounter;

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

    public ProfileInfo(){
        this.firstName = "";
        this.lastName = "";
        this.username = firstName + " " + lastName;
        this.phone_nb = "";
        this.email_address = "";
        this.description = "";
        this.identity_document = "";
        this.profile_picture_uri = "";
        this.background_picture_uri = "";
        this.already_filled = false;
        this.isFree = true;
        this.current_position[0] = 0.0;
        this.current_position[1] = 0.0;
        this.starsRating = Float.valueOf(4);
    }

    public ProfileInfo(ArrayList<String> arrayList){
        this.firstName = arrayList.get(0);
        this.lastName = arrayList.get(1);
        this.username = firstName + " " + lastName;
        this.phone_nb = arrayList.get(2);
        this.email_address = arrayList.get(3);
        //this.current_location
        this.description = arrayList.get(5);
        this.identity_document = arrayList.get(6);
        this.profile_picture_uri = arrayList.get(7);
        this.background_picture_uri = arrayList.get(8);
        this.already_filled = true;
        this.isFree = true;
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

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double[] getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(double[] current_position) {
        this.current_position = current_position;
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

    public void setIdentity_document(String identity_document) {
        this.identity_document = identity_document;
    }

    public String getIdentity_document() {
        return identity_document;
    }

    public void setProfile_picture_uri(String profile_picture_uri) {
        this.profile_picture_uri = profile_picture_uri;
    }

    public String getProfile_picture_uri() {
        return profile_picture_uri;
    }

    public void setBackground_picture_uri(String background_picture_uri) {
        this.background_picture_uri = background_picture_uri;
    }

    public String getBackground_picture_uri() {
        return background_picture_uri;
    }

    public void setAlready_filled(boolean already_filled) {
        this.already_filled = already_filled;
    }

    public boolean isAlready_filled() {
        return already_filled;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> al = new ArrayList<String>();
        al.add(this.firstName);
        al.add(this.lastName);
        al.add(this.phone_nb);
        al.add(this.email_address);
        al.add(""); //this.current_position
        al.add(this.description);
        al.add(identity_document);
        al.add(profile_picture_uri);
        al.add(background_picture_uri);

        return al;
    }
}

