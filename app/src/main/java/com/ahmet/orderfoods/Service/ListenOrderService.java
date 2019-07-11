package com.ahmet.orderfoods.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Model.Request;
import com.ahmet.orderfoods.R;
import com.ahmet.orderfoods.UIMain.OrderStatusActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ListenOrderService extends Service implements ChildEventListener {

    private DatabaseReference mReferenceRequest;



    public ListenOrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mReferenceRequest = FirebaseDatabase.getInstance().getReference().child("Request");

        Common.createNotificationChannel(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mReferenceRequest.addChildEventListener(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        Request request = dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(), request);
    }

    private void showNotification(String key, Request request) {

        Common.createNotificationChannel(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), OrderStatusActivity.class);
        intent.putExtra("userPhone", request.getPhone());
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0, intent,
                                                                    PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), Common.CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker("AHMET")
                .setContentTitle("Update Order")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentInfo("Your order was updated")
                .setContentText("Order # " + key + " was updated status to " + Common.convertStatus(request.getStatus()))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(null))
                .setContentIntent(pendingIntent)
                .setContentInfo("Information");

       // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Notification notification = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());
            managerCompat.notify(Common.NOTIFICATION_ID, notification);
       // }


    }


    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
