package com.example.lab1.model;

import java.util.ArrayList;

public class ProfileInfo {
    private String username;
    private String phone_nb;
    private String email_address;
    private String address;
    private String description;
    private String identity_document;
    private String profile_picture_uri;
    private String background_picture_uri;
    private boolean already_filled;

    public ProfileInfo(){
        this.username = "";
        this.phone_nb = "";
        this.email_address = "";
        this.address = "";
        this.description = "";
        this.identity_document = "";
        this.profile_picture_uri = "";
        this.background_picture_uri = "";
        this.already_filled = false;
    }

    public ProfileInfo(ArrayList<String> arrayList){
        this.username = arrayList.get(0);
        this.phone_nb = arrayList.get(1);
        this.email_address = arrayList.get(2);
        this.address = arrayList.get(3);
        this.description = arrayList.get(4);
        this.identity_document = arrayList.get(5);
        this.profile_picture_uri = arrayList.get(6);
        this.background_picture_uri = arrayList.get(7);
        this.already_filled = true;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
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
        al.add(this.username);
        al.add(this.phone_nb);
        al.add(this.email_address);
        al.add(this.address);
        al.add(this.description);
        al.add(identity_document);
        al.add(profile_picture_uri);
        al.add(background_picture_uri);

        return al;
    }
}

