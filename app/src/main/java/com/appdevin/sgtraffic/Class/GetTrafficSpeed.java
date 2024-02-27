package com.appdevin.sgtraffic.Class;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * Created by Svishnu on 2018-07-24.
 */

public class GetTrafficSpeed {
    //Default Calling
    public GetTrafficSpeed(){}

    //Overload Calling
    //For getting speed info
    public GetTrafficSpeed(Context Con) throws IOException {

        //Creating the queue
        RequestQueue queue= Volley.newRequestQueue(Con);

        //The URL is used browser
       final String URL ="http://datamall2.mytransport.sg/ltaodataservice/TrafficSpeedBands";

       //Intializing
        OkHttpClient client = new OkHttpClient();

        //Building and setting the parameter
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(URL)
                .get()
                .addHeader("AccountKey", "pUwYDn2WSweWv4si91klGg==")
                .build();

        //Calling the request and putting into queue
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                //Getting the json into string
                String resStr=response.body().string().toString();
                try {
                    //Changing the string into jsonObject
                    JSONObject json =new JSONObject(resStr);

                    //Getting the value which as all the info
                    JSONArray a=json.getJSONArray("value");

                    //For loop to loop around all the json in the array
                    for (int i=0;i<a.length();i++)
                    {
                        //Getting each json using the index
                        json=a.getJSONObject(i);

                        //Output
                        String LinkID=json.getString("LinkID");
                        Log.d("LinkID",LinkID);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }
    }


