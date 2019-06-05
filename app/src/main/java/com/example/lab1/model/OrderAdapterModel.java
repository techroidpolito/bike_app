package com.example.lab1.model;

import android.graphics.Picture;

import java.io.Serializable;

public class OrderAdapterModel implements Serializable {
    private String food_name;
    private int food_count;
    private String food_photo; //jpg, how to use it ?
    private String food_id;

    public OrderAdapterModel(String food_name, int food_count, String food_photo, String food_id) {
        this.food_name = food_name;
        this.food_count = food_count;
        this.food_photo = food_photo;
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_count() {
        return food_count;
    }

    public void setFood_count(int food_count) {
        this.food_count = food_count;
    }

    public String getFood_photo() {
        return food_photo;
    }

    public void setFood_photo(String food_photo) {
        this.food_photo = food_photo;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }
}
