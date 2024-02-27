package com.appdevin.sgtraffic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appdevin.sgtraffic.Class.GetAccidentData;
import com.appdevin.sgtraffic.Class.GetTrafficSpeed;
import com.appdevin.sgtraffic.Services.InterentConnection;
import com.appdevin.sgtraffic.Class.Users;
import com.appdevin.sgtraffic.Services.NotificationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

import at.markushi.ui.CircleButton;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText Password;
    EditText Username;
    Button Register;
    FirebaseAuth Log_in=FirebaseAuth.getInstance();
    Button ForgetButton;

   public static DatabaseReference mPostReference;

   Context CON=Login.this;

    public static Users GetData;

    ValueEventListener Listen;

    ProgressBar Load;

    Boolean in=true;
    ProgressDialog Ldialog;

    public static ArrayList<GetAccidentData> GAD = new ArrayList<>();
    GetAccidentData data;

    ImageView logo;
    Animation bottom;

    RelativeLayout LoginLay,ButtonLay;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LoginLay.setVisibility(View.VISIBLE);
            ButtonLay.setVisibility(View.VISIBLE);
        }
    };

    //List of Accident
    CircleButton LAccident;
    //Report Accident
    CircleButton RAccident;

    GetTrafficSpeed speed=new GetTrafficSpeed();

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To make it visible
        LoginLay =  findViewById(R.id.LoginLay);
        ButtonLay =findViewById(R.id.ButtonLay);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        InterentConnection.con=this;
        //Starting the Service
        startService(new Intent(this, NotificationService.class));
        //startService(new Intent(this, InterentConnection.class));

        Log.d("Context", Login.this.toString());
        //Getting ID
        Button Login = findViewById(R.id.btnAuth);

        //Getting ID
        Password = findViewById(R.id.txtPassword);
        Username = findViewById(R.id.txtUsername);
        Register = findViewById(R.id.Register);
        ForgetButton=findViewById(R.id.ForgetPassword);

        Username.setText("studises@gmail.com");
        Password.setText("12345678");

        Ldialog = new ProgressDialog(Login.this);

        //Setting OnClickListener
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        ForgetButton.setOnClickListener(this);

        try {
            GetTrafficSpeed speed=new GetTrafficSpeed(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Check for text change in password
        TextWatcher TPasschange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NullCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Check for text change in Username
        TextWatcher TUnamechange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NullCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Linking the TextChange
        Password.addTextChangedListener(TPasschange);
        Username.addTextChangedListener(TUnamechange);

        //Get accident info
        GetData();

        GAD.clear();
    }


    /*-----------------------------------OnClick Listener-----------------------------------*/

    @Override
    public void onClick(View v) {

        //Firebase
        Log_in=FirebaseAuth.getInstance();
        Log_in.signOut();

        //Variables
        String Uname;
        String Pass;

        //Setting into the variables
        Uname=Username.getText().toString();
        Pass=Password.getText().toString();


        //When Image Button is Clicked
        if(v.getId()==R.id.btnAuth&& NullCheck())
        {
            SignInWithEmail(Uname,Pass);

        }
        else if(!NullCheck())
            Toast.makeText(Login.this,"The text box is empty",Toast.LENGTH_LONG);
        else
            Toast.makeText(Login.this,"Error Logging in",Toast.LENGTH_LONG);

        if(v.getId()==R.id.Register)
        {
            Intent Register =new Intent(Login.this, com.appdevin.sgtraffic.Register.class);
            startActivity(Register);
        }

        if(v.getId()==R.id.ForgetPassword)
        {
            final AlertDialog.Builder build=new AlertDialog.Builder(CON);
            final View LayoutInflate=getLayoutInflater().inflate(R.layout.forget_password,null);


            LayoutInflate.animate();
            build.setView(LayoutInflate);
            dialog=build.create();
            dialog.show();
            final Button LforgetPassword=LayoutInflate.findViewById(R.id.LForgetPassword);

            LforgetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText Email=LayoutInflate.findViewById(R.id.FEmailAddress);
                    ForgotPassword(Email.getText().toString());
                    dialog.dismiss();
                }
            });
        }
    }


    /*-----------------------------------Null Check-----------------------------------*/
    //Check if it is NUll
    public boolean NullCheck()
    {
        //Getting ID
        Password=findViewById(R.id.txtPassword);
        Username=findViewById(R.id.txtUsername);

        //Variables
        String Uname;
        String Pass;

        //Setting into the variables
        Uname=Username.getText().toString();
        Pass=Password.getText().toString();

        //Changing the background to the default
        Username.setBackgroundResource(R.drawable.roundedet_login);
        Password.setBackgroundResource(R.drawable.roundedet_login);


        if(Uname.isEmpty())
            Username.setBackgroundResource(R.drawable.errorlogin);
        if(Pass.isEmpty())
            Password.setBackgroundResource(R.drawable.errorlogin);
        if(Uname.isEmpty()&&Pass.isEmpty())
            return false;

        return true;

    }


    /*-----------------------------------Get User data to firebase-----------------------------------*/
    //Getting Data from Firebase
    public void Data()
    {
        mPostReference = FirebaseDatabase.getInstance().getReference();

         Listen=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GetData=dataSnapshot.child("Users").child(Log_in.getCurrentUser().getUid()).getValue(Users.class);
                GetData.UID=Log_in.getCurrentUser().getUid();
                Log.d("UID",Log_in.getCurrentUser().getUid());
                if(in) {
                    in=false;
                    Intent Map = new Intent(Login.this, com.appdevin.sgtraffic.GMap.class);
                    startActivity(Map);
                    finish();
                    Ldialog.cancel();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostReference.addValueEventListener(Listen);
    }

    /*-----------------------------------Get Accident data to firebase-----------------------------------*/

    public void GetData()
    {
        //Initializing firebase
        mPostReference=FirebaseDatabase.getInstance().getReference("Accidents");


        //Add listener to the value to check for changes
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //To get child of the Accidents
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    //To get the child from the child of Accident
                    for (DataSnapshot PS: postSnapshot.getChildren()) {

                        //Setting the value into the class
                       GetAccidentData data=PS.getValue(GetAccidentData.class);

                       //Adding the set value into a array
                        GAD.add(data);

                        Log.d("Hi","Added");



                    }


                }
                //To check ifo is correct
                for (GetAccidentData da:GAD){
                    Log.d("Lat", String.valueOf(da.AccidentType+ ""+da.LATITUDE));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /*-----------------------------------Signing to firebase-----------------------------------*/
    //Logging for firebase Auth
    public void SignInWithEmail(String Uname,String Pass)
    {
        Log.d("Context",getApplicationContext().toString());

        Ldialog.setMessage("Logging In");
        Ldialog.setCancelable(false);
        Ldialog.setCanceledOnTouchOutside(false);
        Ldialog.show();



        FirebaseAuth Login=FirebaseAuth.getInstance();

        //Login
        Login.signInWithEmailAndPassword(Uname.trim(),Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Log.i("Login","Scuessful");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Also check if the email has been verified
                    if (user != null && IsEmailVerified()) {
                        // User is signed in
                        String UserId = Log_in.getCurrentUser().getUid();
                        Log.d("UIDD",UserId);

                        Log.d("Verified","Nope");

                        Data();
                    }
                    //If the email is not verified
                    else if(!IsEmailVerified()){
                        Log.d("Verified","Nope");
                        Toast.makeText(Login.this,"Please verify your email",Toast.LENGTH_SHORT).show();
                        Ldialog.cancel();
                    }

                    else {
                        Log.d("UID","Error");
                        Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                        Ldialog.cancel();
                    }
                }



            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Ldialog.cancel();
                Toast.makeText(Login.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                Log.i("Login",e.getLocalizedMessage());
            }
        });



    }

    //Logic to check if it is verified
    public boolean IsEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.isEmailVerified())
            return true;
        else
            return false;
    }

    public void ForgotPassword(String Email)
    {
        FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Password Reset", "Email sent.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Password Reset", "Email failed to sent.Error:"+e.getLocalizedMessage());
                //Error Message for user
                Toast.makeText(CON,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }









}
