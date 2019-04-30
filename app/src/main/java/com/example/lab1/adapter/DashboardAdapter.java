package com.example.lab1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.DashboardAdapterModel;
import com.example.lab1.util    .interfaces.OnItemClickListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAdapter  extends RecyclerView.Adapter {

    private int DELIVERY = 1;
    private int POSITION = 2;
    private int PROFILE = 3;
    private ArrayList<DashboardAdapterModel> dashboardAdapterModel = new ArrayList<>();
    private DeliveryItemViewHolder deliveryItemViewHolder;
    private PositionItemViewHolder positionItemViewHolder;
    private ProfileItemViewHolder profileItemViewHolder;
    private OnItemClickListener mListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public DashboardAdapter(ArrayList<DashboardAdapterModel> dashboardAdapterModel) {
        this.dashboardAdapterModel=dashboardAdapterModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == DELIVERY){
            itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.dashboard_item, null);
            return new DashboardAdapter.DeliveryItemViewHolder(itemView);
        }
        else if (viewType == POSITION){
            itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.dashboard_item, null);
            return new DashboardAdapter.PositionItemViewHolder(itemView);
        }
        else if (viewType == PROFILE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.dashboard_item, null);
            return new DashboardAdapter.ProfileItemViewHolder(itemView);
        }
        else{
            itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.dashboard_item, null);
            return new DashboardAdapter.ProfileItemViewHolder(itemView);
        }
    }

    private class DeliveryItemViewHolder extends RecyclerView.ViewHolder {
        TextView textTv;
        ImageView iconImg;
        RelativeLayout cardRl;
        DeliveryItemViewHolder(View v) {
            super(v);
            textTv = v.findViewById(R.id.textTv);
            iconImg = v.findViewById(R.id.iconImg);
            cardRl = v.findViewById(R.id.cardRl);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    private class PositionItemViewHolder extends RecyclerView.ViewHolder {
        TextView textTv;
        ImageView iconImg;
        RelativeLayout cardRl;
        PositionItemViewHolder(View v) {
            super(v);
            textTv = v.findViewById(R.id.textTv);
            iconImg = v.findViewById(R.id.iconImg);
            cardRl = v.findViewById(R.id.cardRl);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    private class ProfileItemViewHolder extends RecyclerView.ViewHolder {
        TextView textTv;
        ImageView iconImg;
        RelativeLayout cardRl;
        ProfileItemViewHolder(View v) {
            super(v);
            textTv = v.findViewById(R.id.textTv);
            iconImg = v.findViewById(R.id.iconImg);
            cardRl = v.findViewById(R.id.cardRl);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DashboardAdapterModel dashboardAdapterModels = dashboardAdapterModel.get(position);
        Integer layoutType = dashboardAdapterModels.getLayoutType();
        if(layoutType== DELIVERY){
            deliveryItemViewHolder = (DashboardAdapter.DeliveryItemViewHolder) holder;
            deliveryItemViewHolder.textTv.setText(dashboardAdapterModels.getText());
            deliveryItemViewHolder.iconImg.setImageResource(dashboardAdapterModels.getDh_icon());
            deliveryItemViewHolder.cardRl.setBackgroundResource(dashboardAdapterModels.getBg_color());
        }
        if(layoutType== POSITION){
            positionItemViewHolder = (DashboardAdapter.PositionItemViewHolder) holder;
            positionItemViewHolder.textTv.setText(dashboardAdapterModels.getText());
            positionItemViewHolder.iconImg.setImageResource(dashboardAdapterModels.getDh_icon());
            positionItemViewHolder.cardRl.setBackgroundResource(dashboardAdapterModels.getBg_color());

        }
        if(layoutType== PROFILE){
            profileItemViewHolder = (DashboardAdapter.ProfileItemViewHolder) holder;
            profileItemViewHolder.textTv.setText(dashboardAdapterModels.getText());
            profileItemViewHolder.iconImg.setImageResource(dashboardAdapterModels.getDh_icon());
            profileItemViewHolder.cardRl.setBackgroundResource(dashboardAdapterModels.getBg_color());
        }
    }

    @Override
    public int getItemCount() {
        return dashboardAdapterModel.size();
    }
    @Override
    public int getItemViewType(int position) {
        return dashboardAdapterModel.get(position).getLayoutType();
    }
}

