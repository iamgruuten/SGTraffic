package com.appdevin.sgtraffic.Class;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.os.Handler.Callback;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appdevin.sgtraffic.GMap;
import com.appdevin.sgtraffic.Map;
import com.appdevin.sgtraffic.R;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by JEYAVISHNU on 18/6/18.
 */

public class OneMap {
    //Variables
    String Token="000";
    String Timestamp;
    public String Road;



    CallBack con;

    /*-----------------------------------Getter-----------------------------------*/
    public String getToken() {
        return Token;
    }

    public String getTimestamp() {
        return Timestamp;
    }



    /*-----------------------------------Callback-----------------------------------*/
    public interface CallBack {
        void data(String data);
    }


    //Getting token and timestamp
    public String Auth(Context context,final CallBack con)
    {
        //Calling the queue
        RequestQueue queue = Volley.newRequestQueue(context);
        this.con = con;

        //The URL is used browser
        final String URL = "https://developers.onemap.sg/privateapi/auth/post/getToken";
        //The JSONObject is to Put Headers
        JSONObject jsonBody = new JSONObject();


        try {
        jsonBody.put("email", "Jeyavishnu22@yahoo.com");
        jsonBody.put("password", "ITe12345");

        JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //Data that was retrived for the json
                    Token=response.getString("access_token");
                    Timestamp=response.getString("expiry_timestamp");

                    con.data(Token);
                    Log.w("Token",Token);
                    Log.d("Timestamp",Timestamp);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("TokenError",e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.getLocalizedMessage());

            }
        }) {

        };
        queue.add(jsonOblect);


    } catch (JSONException e) {
        e.printStackTrace();
        Log.d("ParaError",e.getLocalizedMessage());
    }
        return Token;
}


//    //Used when search clicked using Serach Value
//    public String GetAddressUsingSearch(Context context,String address,final Has a callback) {
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        String URL = "https://developers.onemap.sg/commonapi/search?searchVal=" + "730750" + "&returnGeom=Y&getAddrDetails=Y&pageNum=1";
//
//        Log.d("Items",URL);
//
//
//         JsonObjectRequest j=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONArray a=response.getJSONArray("results");
//                    JSONObject r=a.getJSONObject(0);
//                    Log.d("Lat",r.getString("LATITUDE"));
//                    Log.d("Lng",r.getString("LONGITUDE"));
//                    CBA.SetAddress(r.getString("ROAD_NAME"),Double.parseDouble(r.getString("LATITUDE")),Double.parseDouble(r.getString("LONGITUDE")));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("LatLngError",e.getLocalizedMessage());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("VolleyError",error.getLocalizedMessage());
//            }
//        });
//        queue.add(j);
//         return "";
//    }








}
