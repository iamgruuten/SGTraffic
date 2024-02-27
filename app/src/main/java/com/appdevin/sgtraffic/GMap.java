package com.appdevin.sgtraffic;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appdevin.sgtraffic.Class.CSpeed;
import com.appdevin.sgtraffic.Class.CallBack;

import com.appdevin.sgtraffic.Class.GetAccidentData;
import com.appdevin.sgtraffic.Class.SetAccidentData;
import com.appdevin.sgtraffic.Class.Users;
import com.appdevin.sgtraffic.Class.LocationInfo;
import com.appdevin.sgtraffic.Class.OneMap;
import com.appdevin.sgtraffic.Services.NotificationService;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class GMap extends FragmentActivity implements OnMapReadyCallback,CallBack, OneMap.CallBack, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton floatmenu;

    //Parameter to get Current Location
    private LocationManager locationManger;
    private LocationListener locationListener;

    //Calling the Spinner
    Spinner spinMonth, spinDays, spinYear;

    //Calling the Class
    LocationInfo Locate = new LocationInfo();
    OneMap Token = new OneMap();
    Users getData = Login.GetData;

    //Setting LatLng
    LatLng currentLocation;

    //Calling the variable get last known location
    private FusedLocationProviderClient mFusedLocationClient;

    //Call the callback interface
    CallBack LatLng = this;


    //Creating a List
    List<Uri> Images = new ArrayList<Uri>();


    //Layout variables
    EditText FirstPara;
    EditText SecondPara;
    Button AddMarker;
    Button CurrentLocation;
    EditText LAccidentType;
    EditText LLicensenumber;
    EditText LAddress;
    EditText LDescription;
    Button LUpload;
    LinearLayout UploadLayout;
    FloatingActionButton btnMic;
    CircleButton btnMicBack;

    double lat;
    double lng;

    AlertDialog dialog;


    boolean EnteredNullCheck;


    Boolean btnMicClick;

    String ErrorMessage = "";
    Boolean in = false;

    private String URL;


    String Address = "";


    FirebaseAuth Log_in = FirebaseAuth.getInstance();

    String BirthD;

    JSONObject r;


    double Lat;
    double Lng;

    //For Accident Infomation
    String AccidentType;
    String Licensenumber;
    String AAddress;
    String Description;

    Boolean Temp = true;

    //Speedometer tracker
    public static Button speed;
    public static float speedo;
    public static TextView LargeSpeed;
    ImageButton close;
    ViewGroup container;
    CSpeed myLocation;

    private int REQUEST_MICROPHONE = 1;

    ArrayList<String> matches;

    LayoutInflater inflater;
    private DrawerLayout mRelativeLayout;
    private PopupWindow mPopupWindow;

    StorageReference storage;

    String iarray;

    //ArrayList for Accident info
    public static ArrayList<GetAccidentData> AccidentData = new ArrayList<>();

    DatabaseReference data;

    int check = 0;

    int childcount;
    int limitcount;

    FloatingActionButton FloatAdd;

    RelativeLayout Really;

    //List of Accident
    CircleButton LAccident;
    //Report Accident
    CircleButton RAccident;

    CountDownTimer countDown;

    //For getting time
    Calendar calendar;

    boolean IsNight;

    //Accident Key
    String Key;

    //Animation
    Animation slide_down;
    Animation slide_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);


        //To set the orientation to portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Activating the  method to start the Listener
        GetUpdatedLocation();
        GetCurrentLocation();
        createLocationRequest();

        speed = findViewById(R.id.speedometer);
        mRelativeLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        try {
            slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.appearspeed);

            slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.disappear);
            inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            Toast.makeText(GMap.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        container = (ViewGroup) inflater.inflate(R.layout.activity_speed, null);
        close = container.findViewById(R.id.ib_close);
        LargeSpeed = container.findViewById(R.id.tv);
        close = container.findViewById(R.id.ib_close);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        navigationView = findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        floatmenu = findViewById(R.id.drawer);

        //Side Navigation
        final NavigationView navigationView = findViewById(R.id.navigation);
        View header = navigationView.getHeaderView(0);
        TextView text = header.findViewById(R.id.WelcomeEmail);
        final CircleImageView profile = header.findViewById(R.id.profile_image);
        text.setText(getData.Email);

        TextView text1 = (TextView) header.findViewById(R.id.WelcomeName);
        text1.setText("Welcome Back, " + getData.Name);

        //Profile Photo
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child("Profile Photo/" + getData.UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.d("Storage", "True");
                Glide.with(getApplicationContext()).load(uri).into(profile);
                profile.setImageURI(uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("StorageError", exception.getLocalizedMessage());
            }
        });

        //Float menu
        floatmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Button clicked
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPopupWindow = new PopupWindow(container, mRelativeLayout.getWidth(), mRelativeLayout.getHeight() / 12, true);
                    mPopupWindow.setAnimationStyle(R.style.AnimationPopup);
                    ;
                    mPopupWindow.showAtLocation(mRelativeLayout, Gravity.BOTTOM, 10, 0);
                } catch (Exception e) {
                    Toast.makeText(GMap.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
        } catch (Exception e) {
            Toast.makeText(GMap.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Float menu
        floatmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Moving the info of Accident data into Local variable
        AccidentData = Login.GAD;

        //Finding the Float button
        FloatAdd = findViewById(R.id.FloatAdd);
        Really = findViewById(R.id.Really);

        //Finding the ID of both the circle button
        LAccident = findViewById(R.id.ListAccident);
        RAccident = findViewById(R.id.ReportAccident);

        //Need to correct code
        RAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation FromTheBottom = AnimationUtils.loadAnimation(GMap.this, R.anim.fromthebottom);
                LAccident.setVisibility(View.VISIBLE);
                LAccident.setAnimation(FromTheBottom);
                Toast.makeText(GMap.this, "HEllo", Toast.LENGTH_SHORT).show();
            }
        });

        //Floating add button is clicked
        FloatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Really.setVisibility(View.VISIBLE);
                Animation FromTheBottom = AnimationUtils.loadAnimation(GMap.this, R.anim.scalein);
                Really.setAnimation(FromTheBottom);

            }
        });
        /*-----------------------------------Logic to Dark Mode-----------------------------------*/
        //Initiate the calendar
        calendar = Calendar.getInstance();

        //Dark mode setter speed
        RelativeLayout hi = container.findViewById(R.id.rl_custom_layout);
        TextView indicate = container.findViewById(R.id.legend);
        ImageView sindicate = container.findViewById(R.id.speedimg);


        //Format date into time
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");

        try {
            //Parsing the infomation through the formatter to get this "h:mm"
            //Parsing the infomation through the formatter to get this "h:mm"
            Date NTime = formatter2.parse("18:59");
            Date MNTime = formatter2.parse("06:59");
            String currentTime = formatter2.format(calendar.getTime());

            //The current Time
            Date CTime = formatter2.parse(String.valueOf(currentTime));

            //To check if it is 7am or 7pm already
            if (CTime.after(NTime)) {
                IsNight = true;
                hi.setBackgroundResource(R.drawable.nightspeedbg);
                LargeSpeed.setTextColor(Color.parseColor("#BDC7C1"));
                indicate.setTextColor(Color.parseColor("#BDC7C1"));
                sindicate.setBackgroundResource(R.drawable.ic_action_lightspeed);
                close.setImageResource(R.drawable.ic_action_lightclose);
            } else if (CTime.before(MNTime)) {
                IsNight = true;
                hi.setBackgroundResource(R.drawable.nightspeedbg);
                LargeSpeed.setTextColor(Color.parseColor("#BDC7C1"));
                indicate.setTextColor(Color.parseColor("#BDC7C1"));
                sindicate.setBackgroundResource(R.drawable.ic_action_lightspeed);
                close.setImageResource(R.drawable.ic_action_lightclose);
            } else {
                IsNight = false;
                hi.setBackgroundResource(R.drawable.speedbg);
                LargeSpeed.setTextColor(Color.BLACK);
                indicate.setTextColor(Color.BLACK);
                sindicate.setBackgroundResource(R.drawable.ic_action_speed);
                close.setImageResource(R.drawable.ic_action_close);
            }


            //To check info
            Log.d("DarkMode", String.valueOf(IsNight));

        } catch (ParseException e) {
            Log.d("TimeError", e.getLocalizedMessage());
            e.printStackTrace();
        }

        btnMicClick = true;
        btnMic = findViewById(R.id.btnSpeak);
        btnMicBack = findViewById(R.id.btnBack);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    Log.i("Report", matches.get(0));
                    for (int i = 0; i < matches.size(); i++) {
                        if (matches.get(i).toUpperCase().equals("REPORT ACCIDENT")) {
                            Toast.makeText(GMap.this, "Success", Toast.LENGTH_SHORT).show();
                            Nav(true, 0.0, 0.0);
                        }
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnMicClick) {
                    // Speech Recognizer only runs for 5 seconds
                    countDown = new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            btnMicBack.performClick();
                            btnMicBack.setPressed(true);
                            btnMicBack.invalidate();
                            btnMicBack.setPressed(false);
                            btnMicBack.invalidate();
                        }

                        public void onFinish() {
                            // If user exceeds 5 seconds
                            btnMicClick = true;
                            if (matches == null) {
                                Toast.makeText(GMap.this, "Computation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.start();
                    Toast.makeText(GMap.this, "Recording ...\nPress again to stop", Toast.LENGTH_SHORT).show();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    btnMicClick = false;
                } else {
                    countDown.cancel();
                    mSpeechRecognizer.stopListening();
                    btnMicClick = true;
                }
            }
        });


    }

    /*-----------------------Requesting Audio Permission----------------------*/
    private void requestAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(GMap.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*-----------------------------------Activate Dark Mode-----------------------------------*/
        //IF it is between the time
        if (IsNight) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(this, R.raw.googlestyled));

                if (!success) {
                    Log.e("StyleError", "Style parsing failed.");
                }
            } catch (Exception e) {
                Log.e("TryStyleError", "Can't find style. Error: ", e);
            }
        }

        //To show traffic range
        mMap.setTrafficEnabled(true);


        //Calling a class to get token
        Token.Auth(this, this);

        //Adding marker using the Accident data
        AddMarkersFromGetAccidentData();

        AddMarkersRealTime();

        if (IsNight) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(this, R.raw.googlestyled));

                if (!success) {
                    Log.e("StyleError", "Style parsing failed.");
                }
            } catch (Exception e) {
                Log.e("TryStyleError", "Can't find style. Error: ", e);
            }
        }


    }

    //This is to request to enable location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check if the permission when first entered into the APP
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                MyLocationIndicate();
                CameraPostiton(18, lat, lng);

            }

            //Requesting Audio Permission
            if (requestCode == REQUEST_MICROPHONE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
            }

            if (ContextCompat.checkSelfPermission(GMap.this,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestAudioPermission();
            }
        }


    }

    /*-----------------------------------Location-----------------------------------*/

    //This used to get the Blue dot
    public void MyLocationIndicate() {
        //Ignore this error it is not a error
        //This a Api calling to activate the blue dt
        if(mMap != null) {
            mMap.setMyLocationEnabled(true);
        }else
        {
            Toast.makeText(this, "Error has occurred", Toast.LENGTH_SHORT).show();
        }
    }

    //This gets the updated location
    public void GetUpdatedLocation() {
        locationManger = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Is used to set Lat and Lng
                LatLng.LatLng(location.getLatitude(), location.getLongitude());
                if (location != null) {
                    //Is used to set Lat and Lng
                    LatLng.LatLng(location.getLatitude(), location.getLongitude());
                    myLocation.set(location);
                    updateSpeed(myLocation);
                    tester(myLocation);
                    Log.d(" UpdateSpeed","It running onloocationchanged");
                }
                else
                {
                    speed.setText("NULL");
                    LargeSpeed.setText("NULL");
                    Log.d("Running","NOT  Running");

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        //Check for whick API Level
        if (Build.VERSION.SDK_INT < 23) {
            locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            //Check if the permission is given
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // To request for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //Used to update based on parameter
                locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }


    }

    //This uses the Last known location and current location
    public void GetCurrentLocation() {
        //Check for whick API Level
        if (Build.VERSION.SDK_INT < 23) {
            locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            //Check if the permission is given
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //To request for permissin
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //This to get the last know location
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    LatLng.LatLng(location.getLatitude(), location.getLongitude());
                                    myLocation = new CSpeed(location, true);
                                    updateSpeed(myLocation);
                                }
                            }
                        });

            }

        }

    }



    /*-----------------------------------Side Nav Control-----------------------------------*/

    //To call the adding of marker through alert dialog
    public void Nav(boolean show, Double Lat, Double Lng) {
        Log.d("Context", getApplicationContext().toString());


        //Opening the alertdialog using a layout
        AlertDialog.Builder build = new AlertDialog.Builder(GMap.this);
        View view = getLayoutInflater().inflate(R.layout.activity_accident_input, null);
        view.animate();

        //Calling the ids from accident input
        FirstPara = view.findViewById(R.id.FirstPara);
        SecondPara = view.findViewById(R.id.SecondPara);
        AddMarker = view.findViewById(R.id.AddMarker);
        CurrentLocation = view.findViewById(R.id.CurrentLocation);
        LAccidentType = view.findViewById(R.id.TypeOfAccident);
        LLicensenumber = view.findViewById(R.id.Licenseplatenumber);
        LAddress = view.findViewById(R.id.Address);
        LDescription = view.findViewById(R.id.Description);
        LUpload = view.findViewById(R.id.UploadImage);
        UploadLayout = view.findViewById(R.id.ImagesView);


        if (show) {
            //Set in textbox
            FirstPara.setText(Lat.toString());
            SecondPara.setText(Lng.toString());
            LAddress.setText(Address);
        }


        //Spinner
        spinMonth = view.findViewById(R.id.Month);
        spinDays = view.findViewById(R.id.days);
        spinYear = view.findViewById(R.id.spinYear);

        //Setting the years in spinner
        spinnerYears(spinYear);

        //Setting on listener
        AddMarker.setOnClickListener(this);
        CurrentLocation.setOnClickListener(this);
        LUpload.setOnClickListener(this);

        //Check for text change in Lat
        TextWatcher TFirstchange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EnteredNullCheck)
                    AccidentErrorCheck(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Check for text change in Lng
        TextWatcher TSecondchange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EnteredNullCheck)
                    AccidentErrorCheck(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        //Linking the TextChange
        FirstPara.addTextChangedListener(TFirstchange);
        SecondPara.addTextChangedListener(TSecondchange);
        build.setView(view);
        dialog = build.create();
        dialog.show();


    }

    //Event Listener for Navigation Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            int id = item.getItemId();
            switch (id) {
                case R.id.Accident:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Nav(false, 0.0, 0.0);
                    break;

                case R.id.Logout:
                    //Firebase
                    Log_in = FirebaseAuth.getInstance();
                    Log_in.signOut();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                    finish();

                    //Logout
                    break;

                case R.id.ReportAccident:
                    startActivity(new Intent(getApplicationContext(), ReportAccident.class));

                    break;

                case R.id.sub1:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(GMap.this, AccidentDetails.class);
                    GMap.this.startActivity(intent);
                    //Continue code here for Accident - Qs
                    break;

                case R.id.sub2:
                    Toast.makeText(getApplicationContext(), "sub2", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);

                    //Continue code here for Accident - Qs
                    break;


            }
            return true;
        } catch (Exception e) {

            Log.d("Nav Error", e.getLocalizedMessage());
            return true;

        }
    }


    /*-----------------------------------OnClick Listener-----------------------------------*/
    //When a item is clicked
    @Override
    public void onClick(View v) {

        //When Add marker from accident input is clicked
        if (v.getId() == R.id.AddMarker) {
            //The marker will be added through the method AddMarkersRealTime

            //Getting text
            Lat = Double.parseDouble(FirstPara.getText().toString());
            Lng = Double.parseDouble(SecondPara.getText().toString());
            BirthD = spinDays.getSelectedItem() + "/" + spinMonth.getSelectedItem() + "/" + spinYear.getSelectedItem();
            AccidentType = LAccidentType.getText().toString();
            Licensenumber = LLicensenumber.getText().toString();
            AAddress = LAddress.getText().toString();
            Description = LDescription.getText().toString();


            //Calculation to get LatLng
            LatLng latLng = new LatLng(Lat, Lng);

            dialog.cancel();


            CameraPostiton(18, Lat, Lng);

            //Set the data into the firebase(Method)
            SetAccidentData(AAddress, Lat, Lng);

            //Upload the images from the accident
            UploadPhoto();

            Toast.makeText(GMap.this, "Marker Added", Toast.LENGTH_LONG).show();


            Log.d("Button Clicked", "Add Marker");


        }
        //When Current marker from accident input is clicked
        if (v.getId() == R.id.CurrentLocation) {
            //Getting the Address for title of the marker
            Address = GetAddressForMarker(this, Locate.Lat(), Locate.Lng(), Token.getToken(), false, false);

            FirstPara.setText(Locate.Lat().toString());
            SecondPara.setText(Locate.Lng().toString());
            LAddress.setText(Address);

        }
        //Adding image through java
        if (v.getId() == R.id.UploadImage) {

            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(GMap.this);
            builder.setTitle("Profile Picture");

            //SET ITEMS AND THERE LISTENERS
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Take Photo")) {
                        cameraIntent();
                    } else if (items[item].equals("Choose from Library")) {
                        galleryIntent();
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();


        }

    }



    /*-----------------------------------CallBack-----------------------------------*/

    //A callback to get Lat and Lng
    @Override
    public void LatLng(Double Lat, Double Lng) {

        lat = Lat;
        lng = Lng;
        Locate.setLat(Lat);
        Locate.setLng(Lng);

        Log.d("Location", "Lat=" + Lat + "\nLng=" + Lng + "\nLatLng=" + currentLocation);

        //Indicate the location the blue on the Map
        MyLocationIndicate();

        if (Temp)
            CameraPostiton(18, Locate.Lat(), Locate.Lng());

        Temp = false;

    }

    //A callback to call Token
    @Override
    public void data(final String data) {
        //Calling class to enable when pressed for long
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //A event that activates after a long press
                Log.d("Token Data", data);
                GetAddressForMarker(GMap.this, latLng.latitude, latLng.longitude, Token.getToken(), true, false);
            }
        });
        Log.d("CallBack", data);
    }


    /*-----------------------------------Error Checkers-----------------------------------*/
    //Check if it is NUll on the alert dialog
    public boolean AccidentErrorCheck(boolean Isclicked) {
        EnteredNullCheck = true;
        //Changing the background to the default
        FirstPara.setBackgroundResource(R.drawable.roundedet_login);
        SecondPara.setBackgroundResource(R.drawable.roundedet_login);


        if (FirstPara.getText().toString().isEmpty()) {
            FirstPara.setBackgroundResource(R.drawable.errorlogin);
            ErrorMessage = "The Lat textbox is empty";
        }

        if (SecondPara.getText().toString().isEmpty()) {
            SecondPara.setBackgroundResource(R.drawable.errorlogin);
            ErrorMessage = "The Lat and Lng textbox are empty";
        } else {
            if (!IsDouble(FirstPara.getText().toString())) {
                FirstPara.setBackgroundResource(R.drawable.errorlogin);
                ErrorMessage = "The Lat is not a number";
            }

            if (!IsDouble(SecondPara.getText().toString())) {
                SecondPara.setBackgroundResource(R.drawable.errorlogin);
                ErrorMessage = "The Lat and Lng are not a number";
            }
        }
        if (Isclicked) {
            Toast.makeText(GMap.this, ErrorMessage, Toast.LENGTH_SHORT).show();
        }
        if (FirstPara.getText().toString().isEmpty() || SecondPara.getText().toString().isEmpty() || !IsDouble(SecondPara.getText().toString()) || !IsDouble(FirstPara.getText().toString())) {
            return false;
        }


        return true;

    }

    //To check if the String can be changed into a double
    public boolean IsDouble(String Value) {

        try {
            Double.parseDouble(Value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /*-----------------------------------Google Maps Calling-----------------------------------*/
    //Add marker the google map
    public void AddMarker(String title, LatLng LatLng, String Key) {
        Log.d("MapKey", Key);

        mMap.addMarker(new MarkerOptions().
                position(LatLng).//The position combined with Lat and Lng
                title(title + " >").//The Title of the marker
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))//How the marker looks like
                .setTag(Key);//To identify the data

        //TO set on the click listener for the marker title
        mMap.setOnInfoWindowClickListener(this);

    }

    //Gets the address
    public String GetAddressForMarker(Context context, final double Lat, final double Lng, String Token, final Boolean Nav, final Boolean AddData) {
        //Calling the queue
        RequestQueue queue = Volley.newRequestQueue(context);

        URL = "https://developers.onemap.sg/privateapi/commonsvc/revgeocode?location=" + Lat + "," + Lng + "&token=" + Token;

        Address = "";


        Log.d("Items", URL);


        JsonObjectRequest j = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Item", "in");
                try {
                    JSONArray a = response.getJSONArray("GeocodeInfo");
                    r = a.getJSONObject(0);


                    Log.d("Item", "inn");

                    String Block = "";
                    String Road = "";

                    Boolean CheckBlock = r.toString().contains("BLOCK");
                    Boolean CheckRoad = r.toString().contains("ROAD");
                    if (CheckBlock) {
                        Block = "Block " + r.getString("BLOCK") + " ";
                    }
                    if (CheckRoad) {
                        Road = r.getString("ROAD");
                    }
                    if (!CheckBlock && !CheckRoad) {
                        Road = "UNKNOWN LOCATION";
                    }

                    Address = Block + Road;

                    Log.d("Address", Address);
                    Log.d("Item", Address);


                    if (Nav) {
                        Nav(true, Lat, Lng);
                    }

                    LAddress.setText(Address);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException", e.getLocalizedMessage());
                    if (Nav) {
                        Nav(true, Lat, Lng);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("VolleyError", error.getLocalizedMessage());
            }
        });


        queue.add(j);
        return Address;


    }

    public void CameraPostiton(float Zoom, double Lat, double Lng) {
        currentLocation = new LatLng(Lat, Lng);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(currentLocation, Zoom);
        mMap.animateCamera(location);
    }


    /*-----------------------------------Adding Years to Spinner-----------------------------------*/
    //This to give entry to the Years
    public void spinnerYears(Spinner spinYear) {
        int NowYear = Calendar.getInstance().get(Calendar.YEAR);//Getting the current year
        List<String> Years = new ArrayList<>();//List to store Years

        // For Spinner Years
        for (int i = NowYear; i >= NowYear - 120; i--) {
            Years.add(Integer.toString(i));
        }


//Setting adpter on spinner
        ArrayAdapter<String> adpYears = new ArrayAdapter(GMap.this, R.layout.support_simple_spinner_dropdown_item, Years);
        spinYear.setAdapter(adpYears);


    }


    //Setting into firebase
    public void SetAccidentData(String Address, final double Lat, final double Lng) {

        Log.d("SetData", "True");
        //Calling the DataBase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Accidents");

        //Getting a unique Key
        Key = database.push().getKey();

        //Creating a unique a ID
        iarray = Calendar.getInstance().getTime().toString();

        //Setting info into a class
        SetAccidentData newAccident = new SetAccidentData(Address, Lat, Lng, BirthD, AccidentType, Licensenumber, Description, Key);

        //Setting info into Database
        database.child(Address).child(iarray + " + " + Licensenumber).setValue(newAccident);


    }

    /*-----------------------------------Camera-----------------------------------*/
    //CHOOSE CAMERA
    private void cameraIntent() {


        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 3);
    }

    //CHOOSE IMAGE FROM GALLERY
    private void galleryIntent() {


        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if (requestCode == 2 && resultCode == RESULT_OK) {
            AddImage(data);

        }
        //T0 be done later
//        else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
//            //SAVE URI FROM CAMERA
//           Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            Profile.setImageBitmap(imageBitmap);
//
//
//        }

    }

    //Adds image into the image view
    public void AddImage(Intent data) {

        Images.add(data.getData());
        ImageView myImage = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;
        myImage.setLayoutParams(params);
        myImage.setImageURI(data.getData());
        UploadLayout.addView(myImage);
    }

    //Sending Photos to firebase storage
    public void UploadPhoto() {
        int i = 1;
        //Creating a unique a ID
        iarray = Calendar.getInstance().getTime().toString();
        for (Uri U : Images) {

            storage = FirebaseStorage.getInstance().getReference();

            StorageReference PutFile = storage.child("Accident Photo").child(iarray + " + " + Licensenumber).child(String.valueOf(i));

            PutFile.putFile(U).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Uploaded", "true");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Uploaded", e.getLocalizedMessage());
                }
            });

            i++;
        }


    }

    /*-----------------------------------Adding marker using the Accident data-----------------------------------*/

    //Using existing data to add
    public void AddMarkersFromGetAccidentData() {
        //Variable for LatLng
        LatLng latlng;

        for (GetAccidentData G : AccidentData) {
            //To get new LatLng
            latlng = new LatLng(G.LATITUDE, G.LONGTITUDE);
            AddMarker(G.Address, latlng, G.Key);
        }

    }

    //Please comment this section
    public void AddMarkersRealTime() {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Accidents");

        childcount = NotificationService.childcount;


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ListenCountt", String.valueOf(limitcount));
                Log.d("ChildCountt", String.valueOf(childcount) + " = " + dataSnapshot.getKey());


                if (limitcount >= childcount) {
                    for (DataSnapshot PS : dataSnapshot.getChildren()) {

                        //Setting the value into the class
                        GetAccidentData data = PS.getValue(GetAccidentData.class);
                        limitcount = 1;

                        if (!IsItTheSame(data)) {
                            //Adding the set value into a array
                            AccidentData.add(data);

                            LatLng latlng = new LatLng(data.LATITUDE, data.LONGTITUDE);

                            AddMarker(data.Address, latlng, data.Key);
                            CameraPostiton(20, data.LATITUDE, data.LONGTITUDE);

                            Log.d("Latte", String.valueOf(data.AccidentType + " || " + data.LATITUDE + " || " + data.LONGTITUDE));
                        }


                    }
                    childcount++;
                }
                limitcount++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Childf", dataSnapshot.getKey());
                //To get the child from the child of Accident
                for (DataSnapshot PS : dataSnapshot.getChildren()) {

                    //Setting the value into the class
                    GetAccidentData data = PS.getValue(GetAccidentData.class);


                    if (!IsItTheSame(data)) {
                        //Adding the set value into a array
                        AccidentData.add(data);

                        LatLng latlng = new LatLng(data.LATITUDE, data.LONGTITUDE);

                        AddMarker(data.Address, latlng, data.Key);
                        CameraPostiton(20, data.LATITUDE, data.LONGTITUDE);

                        Log.d("Latte", String.valueOf(data.AccidentType + " || " + data.LATITUDE + " || " + data.LONGTITUDE));
                    }


                }


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
        //To check ifo is correct
        for (GetAccidentData da : AccidentData) {
            Log.d("Latt", String.valueOf(da.AccidentType + " = " + da.LATITUDE));
        }


        data.addChildEventListener(childEventListener);
        check++;


    }

    public boolean IsItTheSame(GetAccidentData data) {
        for (GetAccidentData da : AccidentData) {
            if (da.Key == data.Key) {
                return true;
            }
        }
        return false;
    }

    //When the marker title is clicked
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("Marker", "hhhh");
        //Initializing the intent
        Intent i = new Intent(this, AccidentInfo.class);

        //Getting the key that will identify the data in the array
        String Key = marker.getTag().toString();

        //Sending info together with intent
        i.putExtra("key", Key);

        startActivity(i);
    }

    //PointLess
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    //-----------------------------------------Speed Method-----------------------------------------
    private void updateSpeed(CSpeed location) {
        Log.d("Running", location.toString());
        // TODO Auto-generated method stub
        Double nCurrentSpeed;
        location.setUseMetricunits(true);
        nCurrentSpeed = location.getSeed();

        Log.d("Test", Long.toString(location.getTime()));
        String strCurrentSpeed = String.format(Locale.US, "%.2f", nCurrentSpeed);

        if (location != null) {
            speed.setText(strCurrentSpeed + " km/hr");
            LargeSpeed.setText(strCurrentSpeed + " km/hr");

        } else {
            speed.setText("NULL");
            LargeSpeed.setText("NULL");
        }
    }

    //--------------------------------------Test Method---------------------------------------------
    private void tester(CSpeed location) //test every submethod works from Cspeed
    {
        if (location != null) {
            Toast.makeText(this, Double.toString(location.callout()), Toast.LENGTH_SHORT).show();
            location.setUseMetricunits(true);
        }
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}




