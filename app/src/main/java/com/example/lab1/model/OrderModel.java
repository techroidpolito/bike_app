package com.example.lab1.model;

import android.os.Parcel;
import android.os.Parcelable;



import java.io.Serializable;

public class OrderModel implements Serializable,Parcelable {
    private String oid;
    private String cid;
    private String rid;
    private String biker_id;
    private String cust_phone;
    private String rest_phone;
    private String biker_phone;
    private String cust_email;
    private String rest_email;
    private String rider_email;
    private String cust_lat;
    private String cust_long;
    private String rest_lat;
    private String rest_long;
    private String biker_lat;
    private String biker_long;
    private FoodType foodType;
    private String item_count;
    private String item_total;
    private String to_pay;
    private String rest_charges;
    private String delivery_fee;
    private String discount;
    private String payment_type;
    private String delivery_date_time;
    private String delivery_type;
    private String rider_photo_url;
    private String rider_name;
    private String rest_photo_url;
    private Boolean rating;
    private Boolean rider_accepted;
    private Boolean rest_accepted;
    private Boolean delivered_status;
    private String rest_name;
    private String cust_address_type;
    private String cust_name;
    private String order_date_time;
    private String order_status;//1)order placed 2)order accepted 3)order declined 4)rider accepted 5)rider declined 6)order picked up 7)order_delivered 8)order cancelled

    public OrderModel() {
    }


    protected OrderModel(Parcel in) {
        oid = in.readString();
        cid = in.readString();
        rid = in.readString();
        biker_id = in.readString();
        cust_phone = in.readString();
        rest_phone = in.readString();
        biker_phone = in.readString();
        cust_email = in.readString();
        rest_email = in.readString();
        rider_email = in.readString();
        cust_lat = in.readString();
        cust_long = in.readString();
        rest_lat = in.readString();
        rest_long = in.readString();
        biker_lat = in.readString();
        biker_long = in.readString();
        foodType = in.readParcelable(FoodType.class.getClassLoader());
        item_count = in.readString();
        item_total = in.readString();
        to_pay = in.readString();
        rest_charges = in.readString();
        delivery_fee = in.readString();
        discount = in.readString();
        payment_type = in.readString();
        delivery_date_time = in.readString();
        delivery_type = in.readString();
        rider_photo_url = in.readString();
        rider_name = in.readString();
        rest_photo_url = in.readString();
        byte tmpRating = in.readByte();
        rating = tmpRating == 0 ? null : tmpRating == 1;
        byte tmpRider_accepted = in.readByte();
        rider_accepted = tmpRider_accepted == 0 ? null : tmpRider_accepted == 1;
        byte tmpRest_accepted = in.readByte();
        rest_accepted = tmpRest_accepted == 0 ? null : tmpRest_accepted == 1;
        byte tmpDelivered_status = in.readByte();
        delivered_status = tmpDelivered_status == 0 ? null : tmpDelivered_status == 1;
        rest_name = in.readString();
        cust_address_type = in.readString();
        cust_name = in.readString();
        order_date_time = in.readString();
        order_status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oid);
        dest.writeString(cid);
        dest.writeString(rid);
        dest.writeString(biker_id);
        dest.writeString(cust_phone);
        dest.writeString(rest_phone);
        dest.writeString(biker_phone);
        dest.writeString(cust_email);
        dest.writeString(rest_email);
        dest.writeString(rider_email);
        dest.writeString(cust_lat);
        dest.writeString(cust_long);
        dest.writeString(rest_lat);
        dest.writeString(rest_long);
        dest.writeString(biker_lat);
        dest.writeString(biker_long);
        dest.writeParcelable(foodType, flags);
        dest.writeString(item_count);
        dest.writeString(item_total);
        dest.writeString(to_pay);
        dest.writeString(rest_charges);
        dest.writeString(delivery_fee);
        dest.writeString(discount);
        dest.writeString(payment_type);
        dest.writeString(delivery_date_time);
        dest.writeString(delivery_type);
        dest.writeString(rider_photo_url);
        dest.writeString(rider_name);
        dest.writeString(rest_photo_url);
        dest.writeByte((byte) (rating == null ? 0 : rating ? 1 : 2));
        dest.writeByte((byte) (rider_accepted == null ? 0 : rider_accepted ? 1 : 2));
        dest.writeByte((byte) (rest_accepted == null ? 0 : rest_accepted ? 1 : 2));
        dest.writeByte((byte) (delivered_status == null ? 0 : delivered_status ? 1 : 2));
        dest.writeString(rest_name);
        dest.writeString(cust_address_type);
        dest.writeString(cust_name);
        dest.writeString(order_date_time);
        dest.writeString(order_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getBiker_id() {
        return biker_id;
    }

    public void setBiker_id(String biker_id) {
        this.biker_id = biker_id;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getRest_phone() {
        return rest_phone;
    }

    public void setRest_phone(String rest_phone) {
        this.rest_phone = rest_phone;
    }

    public String getBiker_phone() {
        return biker_phone;
    }

    public void setBiker_phone(String biker_phone) {
        this.biker_phone = biker_phone;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public String getRest_email() {
        return rest_email;
    }

    public void setRest_email(String rest_email) {
        this.rest_email = rest_email;
    }

    public String getRider_email() {
        return rider_email;
    }

    public void setRider_email(String rider_email) {
        this.rider_email = rider_email;
    }

    public String getCust_lat() {
        return cust_lat;
    }

    public void setCust_lat(String cust_lat) {
        this.cust_lat = cust_lat;
    }

    public String getCust_long() {
        return cust_long;
    }

    public void setCust_long(String cust_long) {
        this.cust_long = cust_long;
    }

    public String getRest_lat() {
        return rest_lat;
    }

    public void setRest_lat(String rest_lat) {
        this.rest_lat = rest_lat;
    }

    public String getRest_long() {
        return rest_long;
    }

    public void setRest_long(String rest_long) {
        this.rest_long = rest_long;
    }

    public String getBiker_lat() {
        return biker_lat;
    }

    public void setBiker_lat(String biker_lat) {
        this.biker_lat = biker_lat;
    }

    public String getBiker_long() {
        return biker_long;
    }

    public void setBiker_long(String biker_long) {
        this.biker_long = biker_long;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public String getTo_pay() {
        return to_pay;
    }

    public void setTo_pay(String to_pay) {
        this.to_pay = to_pay;
    }

    public String getRest_charges() {
        return rest_charges;
    }

    public void setRest_charges(String rest_charges) {
        this.rest_charges = rest_charges;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDelivery_date_time() {
        return delivery_date_time;
    }

    public void setDelivery_date_time(String delivery_date_time) {
        this.delivery_date_time = delivery_date_time;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getRider_photo_url() {
        return rider_photo_url;
    }

    public void setRider_photo_url(String rider_photo_url) {
        this.rider_photo_url = rider_photo_url;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getRest_photo_url() {
        return rest_photo_url;
    }

    public void setRest_photo_url(String rest_photo_url) {
        this.rest_photo_url = rest_photo_url;
    }

    public Boolean getRating() {
        return rating;
    }

    public void setRating(Boolean rating) {
        this.rating = rating;
    }

    public Boolean getRider_accepted() {
        return rider_accepted;
    }

    public void setRider_accepted(Boolean rider_accepted) {
        this.rider_accepted = rider_accepted;
    }

    public Boolean getRest_accepted() {
        return rest_accepted;
    }

    public void setRest_accepted(Boolean rest_accepted) {
        this.rest_accepted = rest_accepted;
    }

    public Boolean getDelivered_status() {
        return delivered_status;
    }

    public void setDelivered_status(Boolean delivered_status) {
        this.delivered_status = delivered_status;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getCust_address_type() {
        return cust_address_type;
    }

    public void setCust_address_type(String cust_address_type) {
        this.cust_address_type = cust_address_type;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public static Creator<OrderModel> getCREATOR() {
        return CREATOR;
    }
}
