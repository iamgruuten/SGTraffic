package com.appdevin.sgtraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.appdevin.sgtraffic.Class.GetAccidentData;

import java.util.ArrayList;

public class AccidentInfo extends AppCompatActivity{

    //ArrayList for Accident info
    public static ArrayList<GetAccidentData> AccidentData=new ArrayList<>();

    //To store Layout variables
    TextView txtDate,txtWhere,txtType,txtDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showaccidentinfo);

        //Getting the infomation that was passed from intent
        Intent i=getIntent();
        String Key=i.getStringExtra("key");

        Log.d("Key from Accident Info",Key);

        //Setting the ArrayList from Login into a Local ArrayList variable
        AccidentData=Login.GAD;

        //Finding the value
        txtDate=findViewById(R.id.date);
        txtDes=findViewById(R.id.accidentdesc);
        txtWhere=findViewById(R.id.locate);
        txtType=findViewById(R.id.type);

       //Loop to get one of item
        for(GetAccidentData data:AccidentData){
            //To get the specific item
            if(Key.equals(data.Key)){
                //Setting the information into the layout
                txtType.setText(data.AccidentType);
                txtWhere.setText(data.Address);
                txtDes.setText(data.Description);
                txtDate.setText(data.BirthD);

            }
        }


    }
}
