package com.appdevin.sgtraffic;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.graphics.Bitmap;

import com.appdevin.sgtraffic.Class.NewUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements View.OnClickListener {
    //Context
    Context CON = Register.this;

    //Firebase Authentication
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthList;


    //Creating Variable for the ID
    EditText Name;
   TextInputEditText Password;
    TextInputLayout Pass;
    EditText Email;
    EditText Nric;
    Button Register;
    Spinner spinMonth;
    Spinner spinDays;
    Spinner spinYear;
    TextView Login;
    TextView ChangeImage;
    CircleImageView Profile;
    public static EditText PhoneNumber;

    //Variable for phone verification
    public static String codeSent;
    public static PhoneAuthProvider.ForceResendingToken mResendToken; //Resendtoken

    //Variables for Data
    Uri imageHoldUri;
    String iName;
    String iPassword;
    String iEmail;
    String iNric;
    public static String BirthD;
    String PH;
    Boolean Clicked = false;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;

    StorageReference storage;

    //List
    List<EditText> txt;

    Uri imageUri;
    String UID;


    Bitmap imageBitmap;

    public static TextView Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Getting ID
        Name = findViewById(R.id.txtName);
        Password = findViewById(R.id.txtPassword);
        Email = findViewById(R.id.txtEmail);
        Nric = findViewById(R.id.txtNric);
        spinMonth = findViewById(R.id.Month);
        spinDays = findViewById(R.id.days);
        spinYear = findViewById(R.id.spinYear);
        Login = findViewById(R.id.txtLogin);
        Register = findViewById(R.id.btnRegister);
        ChangeImage = findViewById(R.id.changeProfile);
        Profile = findViewById(R.id.profile_image);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        Date=findViewById(R.id.Date);


        //Click listener
        Register.setOnClickListener(this);
        Login.setOnClickListener(this);
        Password.setOnClickListener(this);

        //Inserting into the List
        txt=new ArrayList();
        txt.add(Name);
        txt.add(Password);
        txt.add(Email);
        txt.add(Nric);
        txt.add(PhoneNumber);

        Pass=findViewById(R.id.layPass);
        Pass.setHintAnimationEnabled(false);

        //Check for text change to change to default background
        TextWatcher Tchange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Clicked) {
                    //Check for Null value
                    for (EditText txt : txt) {
                        txt.setBackgroundResource(R.drawable.registereditdesign);
                        if (TextUtils.isEmpty(txt.getText().toString().trim())) {
                            txt.setBackgroundResource(R.drawable.errorlogin);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Linking to text change
        Name.addTextChangedListener(Tchange);
        Password.addTextChangedListener(Tchange);
        Email.addTextChangedListener(Tchange);
        Nric.addTextChangedListener(Tchange);



        ChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageloader();
            }
        });

       Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean b) {
               if(b)
               Pass.setHint("");
               else
                   Pass.setHint("Password");

           }
       });


    }

    //Method to decide the option
    private void imageloader() {

        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Profile Picture");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    try {
                        if (ContextCompat.checkSelfPermission(Register.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions(Register.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                        }
                        else{
                            cameraIntent();
                        }
                    } catch (Exception e) {
                        finish();
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();
            }
        }}

    //CHOOSE CAMERA
    private String pictureImagePath = "";
    private void cameraIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(Register.this, Register.this.getApplicationContext().getPackageName() + ".my.package.name.provider", file);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
    }

    //CHOOSE IMAGE FROM GALLERY
    private void galleryIntent() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ){
            //Convert Bitmap to URI
            File imgFile = new File(pictureImagePath);
            imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            File files = createImageFile();
            if (files != null) {
                FileOutputStream fout;
                try {
                    fout = new FileOutputStream(files);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                    fout.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageUri = Uri.fromFile(files);

                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }

        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Profile.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Error Cropping image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //From here onwards if register is done can upload to firebase


    @Override
    public void onClick(View v) {

        Clicked=true;

        //Putting Variables
        iName=Name.getText().toString();
        iPassword=Password.getText().toString();
        iEmail=Email.getText().toString();
        iNric=Nric.getText().toString();
        PH=PhoneNumber.getText().toString();

        if(v.getId()==R.id.layPass)
            Pass.setHint("");
        if(v.getId()==R.id.txtPassword)
        {
            Log.d("Clicked","Password "+Password.getHint()+Pass.getHint());
            Pass.setHint("");
            Password.setHint(" ");

        }

        if(v.getId()==R.id.btnRegister)
        {
                //Creating User
               // CreateData(iEmail, iPassword);
                sendPhoneVerification();
                Intent Verify = new Intent(Register.this, SmsVerification.class);
                startActivity(Verify); //Activity Verification page is open
        }
        if(v.getId()==R.id.txtLogin)
        {
            Intent Login=new Intent(Register.this, com.appdevin.sgtraffic.Login.class);
            startActivity(Login);

        }

    }


/*--------------------------------Method-----------------------------------*/


    //To check if all the Info is correct
    public Boolean ErrorCheck()
    {
        //Variables
        int txtLoop=0;

        String ErrorText="";

        //Check for Null value
        for (EditText txt:txt) {
            txt.setBackgroundResource(R.drawable.registereditdesign);
            if (TextUtils.isEmpty(txt.getText().toString().trim())) {
                txt.setBackgroundResource(R.drawable.errorlogin);
                ErrorText="Please do not leave the box empty";
                txtLoop++;
            }
        }

        if(txtLoop>0){
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }


        //Vaildate Password
        boolean isUppercase = !iPassword.equals(iPassword.toLowerCase());
        boolean isLowercase = !iPassword.equals(iPassword.toUpperCase());
        boolean HasAtLeast8   = iPassword.length() >= 8;


        if(!isUppercase)
        {
            Password.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Password must have Uppercase";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(!isLowercase)
        {
            Password.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Password must have Lowercase";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!HasAtLeast8)
        {
            Password.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Password must have 8 characters";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }

        //To check if is a vaild Email
        if(!iEmail.contains("@")) {
            Email.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Invalid email address";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }

        //To check if is a Real NRIC
        if(!NRICchecker(iNric))
        {
            Nric.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Incorrect NRIC";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check if vaild phone number
        if(TextUtils.isEmpty(PhoneNumber.toString())&&PhoneNumber.length()!=8){
            Nric.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Incorrect Phone Number";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check if the date is empty
        if(TextUtils.isEmpty(Date.getText())){
            Nric.setBackgroundResource(R.drawable.errorlogin);
            ErrorText="Please fill up";
            Toast.makeText(this,ErrorText,Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

    //To check if is a Real NRIC
    public boolean NRICchecker(String NRIC)
    {
        //Variables
        int NRICTotal=0;
        Boolean IN=false;
        int Multiplier=7;
        char LastChar='a';
        char FirstChar='a';
        int count=0;
        int Reminder;


        //Step 1
        for (char c:NRIC.toCharArray()) {
            count++;
            if(Character.isDigit(c)&&!IN)
            {
                NRICTotal +=Integer.parseInt(String.valueOf(c))*2;
                IN=true;
            }
            else if(Character.isDigit(c))
            {
                NRICTotal +=Integer.parseInt(String.valueOf(c))*Multiplier;
                Multiplier--;

            }
            else if(count==1)
            {
                FirstChar=Character.toUpperCase(c);
            }
            else if(count==9)
            {
                LastChar=Character.toUpperCase(c);
            }

        }
        //Step 2
        if(FirstChar=='T'||FirstChar=='G')
            NRICTotal+=4;

        //Step 3
        Reminder=NRICTotal%11;

        //Step 4
        return NricStep4(Reminder,LastChar,FirstChar);

    }

    //This do Step 4
    public Boolean NricStep4(int Reminder,char LastChar,char FirstChar)
    {
        boolean SorT=FirstChar=='T'||FirstChar=='S';
        boolean ForG=FirstChar=='F'||FirstChar=='G';
        if(Reminder==10)
        {
            if((SorT&&LastChar=='A')||ForG&&LastChar=='K')
                return true;
        }
        if(Reminder==9)
        {
            if((SorT&&LastChar=='B')||ForG&&LastChar=='L')
                return true;
        }
        if(Reminder==8)
        {
            if((SorT&&LastChar=='C')||ForG&&LastChar=='M')
                return true;
        }
        if(Reminder==7)
        {
            if((SorT&&LastChar=='D')||ForG&&LastChar=='N')
                return true;
        }
        if(Reminder==6)
        {
            if((SorT&&LastChar=='E')||ForG&&LastChar=='P')
                return true;
        }
        if(Reminder==5)
        {
            if((SorT&&LastChar=='F')||ForG&&LastChar=='Q')
                return true;
        }
        if(Reminder==4)
        {
            if((SorT&&LastChar=='G')||ForG&&LastChar=='R')
                return true;
        }
        if(Reminder==3)
        {
            if((SorT&&LastChar=='H')||ForG&&LastChar=='T')
                return true;
        }
        if(Reminder==2)
        {
            if((SorT&&LastChar=='I')||ForG&&LastChar=='U')
                return true;
        }
        if(Reminder==1)
        {
            if((SorT&&LastChar=='Z')||ForG&&LastChar=='W')
                return true;
        }
        if(Reminder==0)
        {
            if((SorT&&LastChar=='J')||ForG&&LastChar=='X')
                return true;
        }
        return false;
    }


    //To add to Database
    public void Setdata(String UID,String Name,String Email,String Password,String Nric,String BirthD,String PH)
    {
        //Calling the DataBase
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users");

        //Adding the Data
        NewUsers user = new NewUsers(Name, Email,BirthD,Password,Nric,PH);
        db.child(UID).setValue(user);


    }

    /*--------------------------------------------Setting profile pictures------------------------------------------*/
    //Sending Photot to firebase storage
    public void UploadPhoto(Uri uri)
    {
        storage= FirebaseStorage.getInstance().getReference();

        StorageReference PutFile=storage.child("Profile Photo").child(UID);

        PutFile.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Uploaded","true");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Uploaded",e.getLocalizedMessage());
            }
        });

    }

    //Create User
    public void CreateData(final String Email, String Pass)
    {
        //Calling Firebase
        final FirebaseAuth CreateUser =FirebaseAuth.getInstance();

        //Calling FirebaseAuth Create User
        CreateUser.createUserWithEmailAndPassword(Email,Pass).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UID=authResult.getUser().getUid();
                //Getting UID

                if(imageUri!=null)
                {
                    UploadPhoto(imageUri);
                }

                //Send verification for email
                sendVerification();

                //Setting Data
                Setdata(UID,iName,iEmail,iPassword,iNric,BirthD,PH);

                Log.d("UID",UID);
                //Moving to another activity
                Intent Login=new Intent(Register.this,com.appdevin.sgtraffic.Login.class);
                startActivity(Login);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });


    }
    //Creating a temporary photo dir to automatically convert bitmap to URI

    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        String root = this.getDir("my_sub_dir", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if(!myDir.exists()){
            myDir.mkdirs();
        }
        try {
            mFileTemp=File.createTempFile(imageFileName,".jpg",myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mFileTemp;
    }

    //To do email verification
    public void sendVerification()
    {  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Sending user verification
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseAuth.getInstance().signOut();
                    Log.d("Verification","Sent");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Verification","Sent Failed");
            }
        });

    }

    public void sendPhoneVerification()
    {
        String Phone = "+65" + PhoneNumber.getText().toString(); //Singapore Code

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Phone,        // Phone number to verify
                50,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Register.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(Register.this, code, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e)  {
            Log.w("TAG", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                PhoneNumber.setError("Invalid phone number.");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Log.d("TAG", "onCodeSent:" + s);

            // Save verification ID and resending token to use them later
            codeSent = s;
            mResendToken = forceResendingToken;
        }
    };

    /*--------------------------------------------Date------------------------------------------*/
    //Setting date Fragement
    public static class Data extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Getting on crete Date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            return datepickerdialog;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            month =+1;
            BirthD=date+"/"+month +"/"+year;
            Date.setText(BirthD);
            Log.d("BirthDay",BirthD);
        }
    }

    //Run this when clicked
    public void DatePicker(View v) {
        DialogFragment Date = new  Data();
        Date.show(getSupportFragmentManager(),"DateFragement");

    }




}
