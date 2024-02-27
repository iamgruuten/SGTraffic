package com.appdevin.sgtraffic.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.appdevin.sgtraffic.Class.OneMap;

import java.util.Timer;
import java.util.TimerTask;

public class InterentConnection extends Service {

    Boolean in=true;
    public static Context con;
    CallBackAddress CBA;



    public InterentConnection() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Service","Internet Connection");

        Timer timer = new Timer();
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(new CheckForConnection(), 10000, 10000);  // 20 sec timer

        return super.START_STICKY;
    }

    class CheckForConnection extends TimerTask{
        @Override
        public void run() {

            // Timer task makes your service will repeat after every 20 Sec.
            if (isInternetConnection()) {
                if(in){
                    Looper.prepare();
                    in=false;
                }
                Log.d("Service","InternetConnection");
                alertDialog();
                CBA.Alert(isInternetConnection());


            }

        }
    }


    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void alertDialog() {
        android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(con);
        build.setTitle("No Internet Connection")
                .setMessage("Please try to connect to the internet ?").setCancelable(true).show();

    }
    public interface CallBackAddress{
        void Alert(Boolean S);
    }



}
