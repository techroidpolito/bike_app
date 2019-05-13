package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class RidingToRestaurantActivity extends AppCompatActivity{
    private Button arrivedButton;
    private CardView restaurantCv, clientCv;
    private MapView mapView;
    private RelativeLayout clientPaymentRl;
    private TextView restaurantNameTv, restaurantAddressTv, restaurantPhoneNumberTv, paymentTypeTv, priceTv;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv;
    private PendingRequestAdapterModel pendingRequestAdapterModel;
    private String bikerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bikerId = getIntent().getStringExtra("bikerId");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_riding_with_gps);
        arrivedButton = findViewById(R.id.arrivalButton);
        mapView = findViewById(R.id.gpsView);
        restaurantCv = findViewById(R.id.destinationCardView);
        restaurantNameTv = restaurantCv.findViewById(R.id.nameTextView);
        restaurantAddressTv = restaurantCv.findViewById(R.id.addressTextView);
        restaurantPhoneNumberTv = restaurantCv.findViewById(R.id.phoneNumberTextView);
        paymentTypeTv = restaurantCv.findViewById(R.id.paymentTypeTextView);
        priceTv = restaurantCv.findViewById(R.id.priceTextView);
        clientCv = findViewById(R.id.clientInfoCardView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        clientPaymentRl = clientCv.findViewById(R.id.paymentRelativeLayout);

        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RidingToRestaurantActivity.this, PickingUpActivity.class);
                i.putExtra("info",pendingRequestAdapterModel);
                i.putExtra("bikerId",bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (savedInstanceState != null) {
            savedInstanceState.getSerializable("delivery");
        }

        if(getIntent().getSerializableExtra("info")!=null){
            pendingRequestAdapterModel = (PendingRequestAdapterModel) getIntent().getSerializableExtra("info");
            setInfo();
        }
    }

    private void setInfo(){
        restaurantNameTv.setText(pendingRequestAdapterModel.getRestaurantName());
        restaurantAddressTv.setText(pendingRequestAdapterModel.getRestaurantAddress());
        restaurantPhoneNumberTv.setText(pendingRequestAdapterModel.getRestaurantPhoneNumber());
        paymentTypeTv.setText(pendingRequestAdapterModel.getPaymentType());
        String price = Float.toString(pendingRequestAdapterModel.getPrice()) + " € ";
        priceTv.setText(price);

        clientNameTv.setText(pendingRequestAdapterModel.getClientName());
        clientAddressTv.setText(pendingRequestAdapterModel.getClientAddress());
        clientPhoneNumberTv.setText(pendingRequestAdapterModel.getClientPhoneNumber());
        clientPaymentRl.setVisibility(View.GONE);

        if( pendingRequestAdapterModel.getRestaurantDistance() < 1.0 ){ //condition to adapt to the distance unit
            arrivedButton.setVisibility(View.VISIBLE);
        } else {
            arrivedButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("delivery", pendingRequestAdapterModel);
        outState.putString("bikerId",bikerId);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bikerId = savedInstanceState.getString("bikerId");
        pendingRequestAdapterModel = (PendingRequestAdapterModel) savedInstanceState.getSerializable("delivery");

        if(pendingRequestAdapterModel!=null) {
            setInfo();
        }
    }

}
