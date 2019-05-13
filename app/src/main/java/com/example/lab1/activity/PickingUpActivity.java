package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PickingUpActivity extends AppCompatActivity {
    private Button pickupButton;
    private CardView clientCv;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv, paymentTypeTv, priceTv, orderNumberTv;
    private PendingRequestAdapterModel deliveryAdapterModel;
    private String bikerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_picking_up);
        bikerId = getIntent().getStringExtra("bikerId");
        pickupButton = findViewById(R.id.pickUpButton);
        clientCv = findViewById(R.id.clientCardView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        paymentTypeTv = clientCv.findViewById(R.id.paymentTypeTextView);
        priceTv = clientCv.findViewById(R.id.priceTextView);
        orderNumberTv = clientCv.findViewById(R.id.orderNumberTextview);

        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PickingUpActivity.this, RidingToClientActivity.class);
                i.putExtra("info",deliveryAdapterModel);
                i.putExtra("bikerId",bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
