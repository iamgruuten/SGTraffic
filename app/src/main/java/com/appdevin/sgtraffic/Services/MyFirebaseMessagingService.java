package com.appdevin.sgtraffic.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.appdevin.sgtraffic.GMap;
import com.appdevin.sgtraffic.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    int NotificationID;


    public MyFirebaseMessagingService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SendNotification(remoteMessage.getNotification().getBody());
    }

    public void SendNotification(String ContentText) {

        Intent intent=new Intent(this,GMap.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent PI= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notif=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder notify=new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Notify")
                .setContentText(ContentText)
                .setContentTitle("Accident Alert")
                .setSmallIcon(R.mipmap.display)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(PI);


        //To be able to see multiple notification
        notif.notify(NotificationID,notify.build());
        NotificationID++;
    }
}
