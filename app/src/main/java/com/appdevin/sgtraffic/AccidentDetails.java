package com.appdevin.sgtraffic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.appdevin.sgtraffic.Class.AccidentAdapter;
import com.appdevin.sgtraffic.Class.AccidentConnector;
import com.appdevin.sgtraffic.Class.GetAccidentData;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccidentDetails extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Spinner sort;
    Spinner type;
    Spinner date;

    String typeS;
    String dateS;

    List<AccidentConnector> data;
    Login login = new Login();

    //Id for Filter and Sort
    MenuItem FilterItem;

    //Sort
    Boolean ascend;
    List<String> itemFilter = new ArrayList<>();
    List<Integer> temp = new ArrayList<>();

    Dialog dialog;

    RecyclerView recyclerView;
    AccidentAdapter adapter = new AccidentAdapter(this, data);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accidentinfo);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intializing the Array List
        data = new ArrayList<>();

        Log.d("GAD Size",String.valueOf(Login.GAD.size()));

        // Setting the data from Login
        Sort(true, "All", dateS);

        adapter = new AccidentAdapter(this, data);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        //Sort and filter
        FilterItem = menu.findItem(R.id.filter);

        FilterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder filter = new AlertDialog.Builder(AccidentDetails.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_filter, null);
                sort = (Spinner) mView.findViewById(R.id.spinnerSort);
                type = (Spinner) mView.findViewById(R.id.spinnerType);
                date = (Spinner) mView.findViewById(R.id.spinnerDate);
                ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(AccidentDetails.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.Sort));
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AccidentDetails.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.Type));
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AccidentDetails.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.Date));
                adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sort.setAdapter(adapter0);
                type.setAdapter(adapter1);
                date.setAdapter(adapter2);

                // Fixes spinner selection when AlertDialog re-opens
                if(ascend)
                {
                    sort.setSelection(0);
                }
                else
                {
                    sort.setSelection(1);
                }

                if(typeS.equals("All"))
                {
                    type.setSelection(0);
                }
                else if(typeS.equals("Title"))
                {
                    type.setSelection(1);
                }
                else
                {
                    type.setSelection(2);
                }

                sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item = parentView.getItemAtPosition(position).toString();

                        if(item.equals("A - Z"))
                        {
                            // Sort in ascending order when spinner item selected.
                            Log.i("AZ", "A - Z");
                            data.clear();
                            temp.clear();
                            itemFilter.clear();
                            Sort(true, typeS, dateS);
                        }
                        else if(item.equals("Z - A"))
                        {
                            // Sort in descending order when spinner item selected.
                            Log.i("ZA", "Z - A");
                            data.clear();
                            temp.clear();
                            itemFilter.clear();
                            Sort(false, typeS, dateS);
                        }
                        else
                        {
                            Log.i("Error", "Error in Sort Spinner");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item = parentView.getItemAtPosition(position).toString();
                        typeS = item;
                        Log.i("Type", typeS);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item = parentView.getItemAtPosition(position).toString();
                        dateS = item;
                        Log.i("Date", dateS);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                filter.setView(mView);
                AlertDialog dialog = filter.create();
                dialog.show();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            newText = newText.toLowerCase();
            ArrayList<AccidentConnector> newList = new ArrayList<>();
            if(typeS.equals("All"))
            {
                for (AccidentConnector accidentConnector : data) {
                    String title = accidentConnector.getAccidentType().toLowerCase();
                    if (title.contains(newText)) {
                        newList.add(accidentConnector);
                    }
                    String title2 = accidentConnector.getDescription().toLowerCase();
                    if (title2.contains(newText)) {
                        newList.add(accidentConnector);
                    }
                }
            }
            else if(typeS.equals("Title"))
            {
                for (AccidentConnector accidentConnector : data) {
                    String title = accidentConnector.getAccidentType().toLowerCase();
                    if (title.contains(newText)) {
                        newList.add(accidentConnector);
                    }
                }
            }
            else
            {
                for (AccidentConnector accidentConnector : data) {
                    String title = accidentConnector.getDescription().toLowerCase();
                    if (title.contains(newText)) {
                        newList.add(accidentConnector);
                    }
                }
            }

            AccidentAdapter adapter = new AccidentAdapter(this, newList);
            recyclerView.setAdapter(adapter);
        }catch (Exception e)
        {
            Toast.makeText(AccidentDetails.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return true;
    }

    //The code that does sorting
    public void Sort(Boolean ascend, String typeS, String dateS){
        this.ascend = ascend;
        this.typeS = typeS;
        this.dateS = dateS;

        // Setting the data from Login
        for(int i = 0; i < Login.GAD.size(); i++)
        {
            itemFilter.add(Login.GAD.get(i).Address);
        }

        if(ascend)
        {
            Collections.sort(itemFilter);
        }
        else
        {
            Collections.sort(itemFilter, Collections.reverseOrder());
        }

        for(int i = 0; i < Login.GAD.size(); i++)
        {
            temp.add(itemFilter.indexOf(Login.GAD.get(i).Address));
        }

        for(int i = 0; i < Login.GAD.size(); i++)
        {
            data.add(new AccidentConnector(Login.GAD.get(temp.get(i)).Address, Login.GAD.get(temp.get(i)).Description));
        }

        AccidentAdapter adapter = new AccidentAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }
}