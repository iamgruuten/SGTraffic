package com.appdevin.sgtraffic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SmsVerification extends AppCompatActivity {

    Button sub;
    EditText number;
    TextView User;
    Register r = new Register();
    FirebaseAuth mAuth;
    Button resend;
    AlertDialog.Builder builder;
    ProgressDialog dialog;
    public static String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        sub = findViewById(R.id.verification);
        number = findViewById(R.id.verPhone);
        User = findViewById(R.id.TraceNumber);
        resend = findViewById(R.id.resend);

        dialog = new ProgressDialog(SmsVerification.this);

        User.setText(Register.PhoneNumber.getText().toString()); //Show Phone number that is sent to contact

        countdown(); //Timeout event

        codeSent = Register.codeSent;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); //Show numeric keyboard

        Button back = findViewById(R.id.park);
        back.setPaintFlags(back.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //Underline the back button

        final String[] option = {"Sms Verification"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmsVerification.this, android.R.layout.select_dialog_item, option){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                    TextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);

                // Return the view
                return view;
            }
        };
        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.MyDialogTheme);
        builder = new AlertDialog.Builder(cw);
        TextView myMsg = new TextView(this);
        myMsg.setText("How do you want to receive the code?");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(16);
        myMsg.setTextColor(Color.BLACK);
        builder.setCustomTitle(myMsg);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case 0:
                        dialog.setMessage("Resending Sms Verification");
                        dialog.show();
                        resendVerificationCode( "65" + Register.PhoneNumber.getText().toString(), Register.mResendToken);
                    break;
                    default:
                        Snackbar.make(findViewById(android.R.id.content), "An Error has occurred. Please Try again", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Verifying");
                dialog.show();
            verifysignin(); //Verify if verification number is valid
            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog a = builder.create();
                a.show();
            }
        });
    }

    public void verifysignin(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, number.getText().toString()); //Comparing both verify code and input value
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SmsVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent Login = new Intent(SmsVerification.this, com.appdevin.sgtraffic.Login.class);
                            startActivity(Login);
                            Toast.makeText(getApplicationContext(),"Verify Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Incorrect Verification Code", Toast.LENGTH_LONG).show();
                            }
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        dialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content), "Resent Successfully", Snackbar.LENGTH_LONG).show();
        countdown();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(SmsVerification.this, code, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("TAG", "onVerificationFailed", e);
            if (e instanceof FirebaseTooManyRequestsException) {
                Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("TAG", "onCodeSent:" + s);
            codeSent = s;
        }
    };

    public void countdown()
    {
     //   resend.setEnabled(false);
        new CountDownTimer(50000, 10) { //Set Timer for 50 seconds
            public void onTick(long millisUntilFinished) {
                resend.setText("Resend code in " + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                resend.setEnabled(true);
                resend.setText("RESEND VERIFICATION CODE");
            }
        }.start();
    }
}
