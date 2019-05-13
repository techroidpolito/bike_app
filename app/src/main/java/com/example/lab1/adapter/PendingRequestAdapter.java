package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.example.lab1.util.interfaces.AcceptClickItemListener;
import com.example.lab1.util.interfaces.RejectClickItemListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PendingRequestAdapter extends RecyclerView.Adapter {
    private ArrayList<PendingRequestAdapterModel> pendingRequestAdapterModel = new ArrayList<>();
    private PendingItemViewHolder pendingItemViewHolder;
    private AcceptClickItemListener acceptClickItemListener;
    private RejectClickItemListener rejectClickItemListener;
    Context context;

    public PendingRequestAdapter(ArrayList<PendingRequestAdapterModel> pendingRequestAdapterModel, Context context) {
        this.pendingRequestAdapterModel=pendingRequestAdapterModel;
        this.context = context;
    }

    public void setAcceptClickItemListener(AcceptClickItemListener listener) {
        acceptClickItemListener = listener;
    }
    public void setRejectClickItemListener(RejectClickItemListener listener) {
        rejectClickItemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.pending_request_item, null);
        return new PendingRequestAdapter.PendingItemViewHolder(itemView);
    }

    private class PendingItemViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantNameTv, restaurantAddressTv, distanceTv, paymentTypeTv, priceTv, accrejTv;
        Button acceptBt, rejectBt;
        PendingItemViewHolder(View v) {
            super(v);
            restaurantNameTv = v.findViewById(R.id.restaurantNameTextView);
            restaurantAddressTv = v.findViewById(R.id.restaurantAddressTextView);
            distanceTv = v.findViewById(R.id.distance_placedTextView);
            paymentTypeTv = v.findViewById(R.id.paymentTypeTextView);
            priceTv = v.findViewById(R.id.priceTextView);
            accrejTv = v.findViewById(R.id.accrejTv);
            acceptBt = v.findViewById(R.id.acceptButton);
            rejectBt = v.findViewById(R.id.rejectButton);
            acceptBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (acceptClickItemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            acceptClickItemListener.onAcceptClick(position);
                        }
                    }
                }
            });
            rejectBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rejectClickItemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            rejectClickItemListener.onRejectClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PendingRequestAdapterModel pendingRequestAdapterModels = pendingRequestAdapterModel.get(position);
        pendingItemViewHolder = (PendingRequestAdapter.PendingItemViewHolder) holder;
        pendingItemViewHolder.restaurantNameTv.setText(pendingRequestAdapterModels.getRestaurantName());
        pendingItemViewHolder.restaurantAddressTv.setText(pendingRequestAdapterModels.getRestaurantAddress());
        String distance = Float.toString(pendingRequestAdapterModels.getRestaurantDistance())+" km";
        pendingItemViewHolder.distanceTv.setText(distance);
        pendingItemViewHolder.paymentTypeTv.setText(pendingRequestAdapterModels.getPaymentType());
        if (pendingRequestAdapterModels.getPaymentType().equals("Cash")){
            String price = Float.toString(pendingRequestAdapterModels.getPrice()) + " € ";
            pendingItemViewHolder.priceTv.setText(price);
        } else {
            pendingItemViewHolder.priceTv.setText("");
        }

        if(pendingRequestAdapterModels.getAccept()==false && pendingRequestAdapterModels.getReject()==false){
            pendingItemViewHolder.acceptBt.setVisibility(View.VISIBLE);
            pendingItemViewHolder.rejectBt.setVisibility(View.VISIBLE);
            pendingItemViewHolder.accrejTv.setVisibility(View.GONE);
        }
        if(pendingRequestAdapterModels.getAccept()==true && pendingRequestAdapterModels.getReject()==false){
            pendingItemViewHolder.acceptBt.setVisibility(View.GONE);
            pendingItemViewHolder.rejectBt.setVisibility(View.GONE);
            pendingItemViewHolder.accrejTv.setVisibility(View.VISIBLE);
            pendingItemViewHolder.accrejTv.setText("You accepted this delivery request.");
            pendingItemViewHolder.accrejTv.setTextColor(ContextCompat.getColor(context, R.color.green_800));
        }
        if(pendingRequestAdapterModels.getAccept()==false && pendingRequestAdapterModels.getReject()==true){
            pendingItemViewHolder.acceptBt.setVisibility(View.GONE);
            pendingItemViewHolder.rejectBt.setVisibility(View.GONE);
            pendingItemViewHolder.accrejTv.setVisibility(View.VISIBLE);
            pendingItemViewHolder.accrejTv.setText("You rejected this delivery request.");
            pendingItemViewHolder.accrejTv.setTextColor(ContextCompat.getColor(context, R.color.red_800));
        }
    }

    @Override
    public int getItemCount() {
        return pendingRequestAdapterModel.size();
    }
}

