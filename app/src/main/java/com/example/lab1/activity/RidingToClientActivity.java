package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.google.android.gms.maps.MapView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RidingToClientActivity extends AppCompatActivity {
    private Button deliveredButton;
    private CardView clientCv, hiddenCv;
    private MapView mapView;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv, paymentTypeTv, priceTv, orderNumberTv;
    private PendingRequestAdapterModel deliveryAdapterModel;
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
        deliveredButton = findViewById(R.id.arrivalButton);
        mapView = findViewById(R.id.gpsView);
        clientCv = findViewById(R.id.destinationCardView);
        hiddenCv = findViewById(R.id.clientInfoCardView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        paymentTypeTv = clientCv.findViewById(R.id.paymentTypeTextView);
        priceTv = clientCv.findViewById(R.id.priceTextView);
        orderNumberTv = clientCv.findViewById(R.id.orderNumberTextview);

        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RidingToClientActivity.this, PendingRequestActivity.class);
                i.putExtra("bikerId",bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        if (savedInstanceState != null) {
            savedInstanceState.getSerializable("delivery");
        }
        if(getIntent().getSerializableExtra("info")!=null){
            deliveryAdapterModel = (PendingRequestAdapterModel) getIntent().getSerializableExtra("info");
            setInfo();
        }
    }

    private void setInfo(){
        clientNameTv.setText(deliveryAdapterModel.getClientName());
        clientAddressTv.setText(deliveryAdapterModel.getClientAddress());
        clientPhoneNumberTv.setText(deliveryAdapterModel.getClientPhoneNumber());
        paymentTypeTv.setText(deliveryAdapterModel.getPaymentType());
        String price = Float.toString(deliveryAdapterModel.getPrice()) + " € ";
        priceTv.setText(price);
        orderNumberTv.setVisibility(View.VISIBLE);
        String orderNumber = "Order n° "+Integer.toString(deliveryAdapterModel.getOrderNumber());
        orderNumberTv.setText(orderNumber);

        hiddenCv.setVisibility(View.GONE);

        deliveredButton.setText(R.string.order_delivered);
        if( deliveryAdapterModel.getClientDistance() < 1.0 ){ //condition to adapt to the distance unit
            deliveredButton.setVisibility(View.VISIBLE);
        } else {
            deliveredButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("delivery", deliveryAdapterModel);
        outState.putString("bikerId",bikerId);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bikerId = savedInstanceState.getString("bikerId");
        deliveryAdapterModel = (PendingRequestAdapterModel) savedInstanceState.getSerializable("delivery");
        if(deliveryAdapterModel!=null) {
            setInfo();
        }
    }
}
