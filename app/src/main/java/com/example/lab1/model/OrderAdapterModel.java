package com.example.lab1.model;

import java.io.Serializable;

public class OrderAdapterModel implements Serializable {
    private String food_name;
    private String food_count;
    private String food_id;

    public OrderAdapterModel(String food_name, String food_count, String food_id) {
        this.food_name = food_name;
        this.food_count = food_count;
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_count() {
        return food_count;
    }

    public void setFood_count(String food_count) {
        this.food_count = food_count;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }
}
