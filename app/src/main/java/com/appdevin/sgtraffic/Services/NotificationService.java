package com.appdevin.sgtraffic.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appdevin.sgtraffic.Class.GetAccidentData;
import com.appdevin.sgtraffic.Login;
import com.appdevin.sgtraffic.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    int NotificationID;
    boolean start=true;
boolean starte=true;
    public static int childcount;
    int limitcount;
    int check=0;
    DataSnapshot dataSnapshotold;

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DatabaseReference data= FirebaseDatabase.getInstance().getReference("Accidents");


        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //To get the Child
                if(start){
                    childcount=(int)dataSnapshot.getChildrenCount();
                }
                //To make sure it dosen't one time only
                starte=true;
                limitcount=0;

                Log.d("ListenCount", "hello");

                    //This will listen to the change in the child of Accident
                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(starte){

                                Log.d("ListenCount", String.valueOf(limitcount));
                                Log.d("ChildCount", String.valueOf(childcount) +" = "+dataSnapshot.getKey());

                                //To make sure the notification no all the child is sent as notification
                                if (limitcount >= childcount) {
                                    //Send notification
                                    PushNotification(dataSnapshot);
                                    Log.d("Notification", "Pushed");
                                    childcount++;
                                }
                                limitcount++;

                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            //Send notification when the child is changed
                            PushNotification(dataSnapshot);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    if(check==0)
                    {
                        data.addChildEventListener(childEventListener);
                        check++;
                    }

                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Log.d("SeriveStarted","NotificationService");
        return super.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //Send Notifcation
    public void PushNotification(DataSnapshot dataSnapshot)
    {   NotificationManager notif=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notify=new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Notify")
                .setContentText("There was a Accident at "+dataSnapshot.getKey())
                .setContentTitle("Accident Alert")
                .setSmallIcon(R.mipmap.display)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationManager.IMPORTANCE_HIGH);

        Log.d("ChildAdded",dataSnapshot.getKey());
        notif.notify(NotificationID,notify.build());
        NotificationID++;


    }


}
