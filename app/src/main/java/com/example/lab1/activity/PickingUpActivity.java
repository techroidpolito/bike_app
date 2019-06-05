package com.example.lab1.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.adapter.OrderAdapter;
import com.example.lab1.model.OrderAdapterModel;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class PickingUpActivity extends AppCompatActivity {
    private Button pickupButton;
    private CardView clientCv;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv, paymentTypeTv, priceTv, orderNumberTv;
    private RecyclerView orderContentRv;
    private RelativeLayout clientPhoneRl;

    private PendingRequestAdapterModel deliveryAdapterModel;
    private String bikerId;
    private DatabaseReference orderReference;

    private OrderAdapter mAdapter;
    private ArrayList<OrderAdapterModel> orderAdapterModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference foodTypeReference;

    private static final int PERMISSION_REQUEST_CALL_PHONE = 2;
    private static final String TAG = PickingUpActivity.class.getSimpleName();

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
        orderContentRv = findViewById(R.id.orderContentRecyclerView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        paymentTypeTv = clientCv.findViewById(R.id.paymentTypeTextView);
        priceTv = clientCv.findViewById(R.id.priceTextView);
        orderNumberTv = clientCv.findViewById(R.id.orderNumberTextview);
        clientPhoneRl = clientCv.findViewById(R.id.phoneNumberRelativeLayout);

        if (savedInstanceState != null) {
            savedInstanceState.getSerializable("delivery");
        }
        if(getIntent().getSerializableExtra("info")!=null) {
            deliveryAdapterModel = (PendingRequestAdapterModel) getIntent().getSerializableExtra("info");
            setInfo();
        }

        orderReference = FirebaseDatabase.getInstance().getReference(getString(R.string.order_details)).child( deliveryAdapterModel.getOrder_id() );
        foodTypeReference = FirebaseDatabase.getInstance().getReference(getString(R.string.foodtype)).child(deliveryAdapterModel.getFoodType_id());

        foodTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderAdapterModel = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String order_id = childDataSnapshot.getKey();
                    Log.d(TAG, order_id + " : " + childDataSnapshot.getValue()); //displays the key for the node
/*
                    HashMap<String,String> order = (HashMap<String, String>) childDataSnapshot.getValue();
                    String fid = order.get(getString(R.string.foodtype_id));
                    String bid = order.get(getString(R.string.bikerID));
                    Boolean rider_accepted = Boolean.valueOf(order.get(getString(R.string.rider_accepted)));

                    if( bid!=null && bid.equals(bikerId) && !rider_accepted){
                        pendingRequestAdapterModel.add(
                                new PendingRequestAdapterModel(
                                        order.get(getString(R.string.restaurant_name)),
                                        order.get(getString(R.string.restaurant_address)),
                                        order.get(getString(R.string.restaurant_phone)),
                                        order.get(getString(R.string.customer_name)),
                                        order.get(getString(R.string.customer_address)),
                                        order.get(getString(R.string.customer_phone)),
                                        order.get(getString(R.string.payment_type)),
                                        Float.parseFloat( order.get(getString(R.string.price)) ),
                                        0,
                                        order_id,
                                        fid
                                )
                        );
                    }*/
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed ", databaseError.getMessage());
            }
        });

        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderReference.child(getString(R.string.order_status)).setValue(String.valueOf(6)); //6 = order picked up

                Intent i = new Intent(PickingUpActivity.this, RidingToClientActivity.class);
                i.putExtra("info",deliveryAdapterModel);
                i.putExtra("bikerId",bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        clientPhoneRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+deliveryAdapterModel.getClientPhoneNumber()));

                if (ActivityCompat.checkSelfPermission(PickingUpActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(PickingUpActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL_PHONE);
                    return;
                }
                startActivity(callIntent);
            }
        });

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

    private void setAdapter() {
        mLayoutManager = new LinearLayoutManager(PickingUpActivity.this);
        mAdapter = new OrderAdapter(orderAdapterModel,PickingUpActivity.this);
        orderContentRv.setLayoutManager(mLayoutManager);
        orderContentRv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
