package com.example.lab1.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FoodType implements Parcelable,Serializable {
    private String foodid;
    private String food_name;
    private String food_type;
    private Boolean food_available;
    private String food_photo;
    private String food_description;
    private String food_cost;
    private String food_rating;
    private String food_quantity;
    private String food_totalprice;
    private String food_time;
    private Boolean non_veg;

    public FoodType(String food_name, String food_type, Boolean food_available, String food_photo, String food_description, String food_cost, String food_rating) {
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_available = food_available;
        this.food_photo = food_photo;
        this.food_description = food_description;
        this.food_cost = food_cost;
        this.food_rating = food_rating;
    }

    public FoodType(String foodid, String food_name, String food_type, Boolean food_available, String food_photo, String food_description, String food_cost, String food_rating) {
        this.foodid = foodid;
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_available = food_available;
        this.food_photo = food_photo;
        this.food_description = food_description;
        this.food_cost = food_cost;
        this.food_rating = food_rating;
    }

    public FoodType(String foodid, String food_name, String food_type, Boolean food_available, String food_photo, String food_description, String food_cost, String food_rating, String food_quantity, String food_totalprice) {
        this.foodid = foodid;
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_available = food_available;
        this.food_photo = food_photo;
        this.food_description = food_description;
        this.food_cost = food_cost;
        this.food_rating = food_rating;
        this.food_quantity = food_quantity;
        this.food_totalprice = food_totalprice;
    }

    public FoodType(String foodid, String food_name, String food_type, Boolean food_available, String food_photo, String food_description, String food_cost, String food_rating, String food_quantity, String food_totalprice, String food_time) {
        this.foodid = foodid;
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_available = food_available;
        this.food_photo = food_photo;
        this.food_description = food_description;
        this.food_cost = food_cost;
        this.food_rating = food_rating;
        this.food_quantity = food_quantity;
        this.food_totalprice = food_totalprice;
        this.food_time = food_time;
    }

    public FoodType() {
    }

    protected FoodType(Parcel in) {
        foodid = in.readString();
        food_name = in.readString();
        food_type = in.readString();
        byte tmpFood_available = in.readByte();
        food_available = tmpFood_available == 0 ? null : tmpFood_available == 1;
        food_photo = in.readString();
        food_description = in.readString();
        food_cost = in.readString();
        food_rating = in.readString();
        food_quantity = in.readString();
        food_totalprice = in.readString();
        food_time = in.readString();
        byte tmpNon_veg = in.readByte();
        non_veg = tmpNon_veg == 0 ? null : tmpNon_veg == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodid);
        dest.writeString(food_name);
        dest.writeString(food_type);
        dest.writeByte((byte) (food_available == null ? 0 : food_available ? 1 : 2));
        dest.writeString(food_photo);
        dest.writeString(food_description);
        dest.writeString(food_cost);
        dest.writeString(food_rating);
        dest.writeString(food_quantity);
        dest.writeString(food_totalprice);
        dest.writeString(food_time);
        dest.writeByte((byte) (non_veg == null ? 0 : non_veg ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodType> CREATOR = new Creator<FoodType>() {
        @Override
        public FoodType createFromParcel(Parcel in) {
            return new FoodType(in);
        }

        @Override
        public FoodType[] newArray(int size) {
            return new FoodType[size];
        }
    };

    public Boolean getNon_veg() {
        return non_veg;
    }

    public void setNon_veg(Boolean non_veg) {
        this.non_veg = non_veg;
    }


    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public Boolean getFood_available() {
        return food_available;
    }

    public void setFood_available(Boolean food_available) {
        this.food_available = food_available;
    }

    public String getFood_photo() {
        return food_photo;
    }

    public void setFood_photo(String food_photo) {
        this.food_photo = food_photo;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public String getFood_cost() {
        return food_cost;
    }

    public void setFood_cost(String food_cost) {
        this.food_cost = food_cost;
    }

    public String getFood_rating() {
        return food_rating;
    }

    public void setFood_rating(String food_rating) {
        this.food_rating = food_rating;
    }

    public String getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(String food_quantity) {
        this.food_quantity = food_quantity;
    }

    public String getFood_totalprice() {
        return food_totalprice;
    }

    public void setFood_totalprice(String food_totalprice) {
        this.food_totalprice = food_totalprice;
    }

    public static Creator<FoodType> getCREATOR() {
        return CREATOR;
    }

    public String getFood_time() {
        return food_time;
    }

    public void setFood_time(String food_time) {
        this.food_time = food_time;
    }
}
