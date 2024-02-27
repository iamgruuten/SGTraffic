package com.appdevin.sgtraffic.Class;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdevin.sgtraffic.AccidentDetails;
import com.appdevin.sgtraffic.AccidentInfo;
import com.appdevin.sgtraffic.Login;
import com.appdevin.sgtraffic.R;

import java.util.ArrayList;
import java.util.List;

public class AccidentAdapter extends RecyclerView.Adapter<AccidentAdapter.AccidentViewHolder> {

    //Needed to inflate the layout
    private Context mCtx;


    //To to store the list of data to display
    private List<AccidentConnector> AccidentData;

    public AccidentAdapter(Context mCtx, List<AccidentConnector> AccidentData) {
        this.mCtx = mCtx;
        this.AccidentData = AccidentData;
    }


    //Bind's with view
    @Override
    public void onBindViewHolder(final AccidentViewHolder holder, final int position) {
        //To know which item it is currently at
        AccidentConnector product = AccidentData.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getAccidentType());
        holder.textViewShortDesc.setText(product.getDescription());

        //Creating a parameter for the layout
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // set height of RecyclerView
        holder.itemView.setLayoutParams(params);

        holder.cardview.setVisibility(View.VISIBLE);

        //Setting the cardview on click listener
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initializing the intent
                Intent i = new Intent(mCtx, AccidentInfo.class);

                //Getting the key that will identify the data in the array
                String Key = Login.GAD.get(position).Key;

                //Sending info together with intent
                i.putExtra("key", Key);

                //Move to accident info
                mCtx.startActivity(i);


            }
        });


    }

    //This method will return the view holder
    @Override
    public AccidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.accidentdetails, null);

        return new AccidentViewHolder(view);
    }

    //Returns the size of the list
    @Override
    public int getItemCount() {
        return AccidentData.size();
    }

    class AccidentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc;
        ImageView imageView;
        CardView cardview;


        public AccidentViewHolder(View itemView) {
            super(itemView);

            //Identitifing the UI
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            imageView = itemView.findViewById(R.id.imageView);
            cardview = itemView.findViewById(R.id.DesCardView);
        }
    }

    public void setFilter(ArrayList<AccidentConnector> newList) {

        AccidentData = new ArrayList<>();
        AccidentData.addAll(newList);
        notifyDataSetChanged();
    }

}
