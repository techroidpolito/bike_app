package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.OrderAdapterModel;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter {
    private ArrayList<OrderAdapterModel> orderAdapterModel = new ArrayList<>();
    private FoodItemViewHolder foodItemViewHolder;
    Context context;

    public OrderAdapter(ArrayList<OrderAdapterModel> orderAdapterModel, Context context) {
        this.orderAdapterModel = orderAdapterModel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.food_item, null);
        return new OrderAdapter.FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderAdapterModel orderAdapterModels = orderAdapterModel.get(position);
        foodItemViewHolder = (OrderAdapter.FoodItemViewHolder) holder;

        foodItemViewHolder.foodNameTv.setText(orderAdapterModels.getFood_name());
        foodItemViewHolder.foodCountTv.setText("Quantity: "+String.valueOf(orderAdapterModels.getFood_count()));
        //foodItemViewHolder.foodPictureIv.setPicture(orderAdapterModels.getFood_picture()); ??????????????????????????????????????????????????????????????????????????????
    }

    @Override
    public int getItemCount() {
        return orderAdapterModel.size();
    }


    public class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTv, foodCountTv;
        ImageView foodPictureIv;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTv = itemView.findViewById(R.id.foodNameTv);
            foodCountTv = itemView.findViewById(R.id.foodCountTv);
            foodPictureIv = itemView.findViewById(R.id.foodPictureIv );
        }
    }
}
