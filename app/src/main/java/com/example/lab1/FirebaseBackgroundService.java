package com.example.lab1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.lab1.activity.PendingRequestActivity;
import com.example.lab1.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseBackgroundService extends Service {
    private SharedPreferences tokenpref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;
    private String email, password, bid, phone;
 //   private Firebase f = new Firebase("https://somedemo.firebaseio-demo.com/");
    private ValueEventListener handler;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        foreground_call("Rider Request");
        tokenpref = getSharedPreferences("tokenpref",MODE_PRIVATE);
        email = tokenpref.getString(getString(R.string.email_id_pref), null);
        password = tokenpref.getString(getString(R.string.password_id_pref), null);
        bid = tokenpref.getString(getString(R.string.bid_id_pref), null);
        dbReference = FirebaseDatabase.getInstance().getReference("order_details");
        Query query = dbReference.orderByChild("bid").equalTo(bid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    OrderModel order_status = postSnapshot.getValue(OrderModel.class);
                    order_status.setOid(postSnapshot.getKey());
                    if(order_status.getOrder_status()!=null){
                        if(order_status.getDelivered_status()){
                                stopForeground(true);
                            }
                        else {
                            String status= null;
                            if(order_status.getOrder_status().equalsIgnoreCase(getString(R.string.order_status_9))){
                                status = "Order is placed and pending with restaurent to accept the order";
                            }
                            if(status!=null){
                                foreground_call(status);
                            }
                            else{
                                stopForeground(true);
                            }

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }


    public void foreground_call(String content) {

        Intent intent = new Intent(getApplicationContext(), PendingRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_location";
        String CHANNEL_NAME = "channel_location";

        NotificationCompat.Builder builder = null;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setChannelId(CHANNEL_ID);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }
        Log.d("Service", "Started");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_launcher));
        builder.setContentTitle("EGAL Delivery");
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();

        startForeground(20, notification);
    }

}