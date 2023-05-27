package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    EditText phone, otp, A, fullname;
    Button btngenOTP, btnverify, signupbtn;
    RadioGroup radiogender;
    RadioButton female, male;
    FirebaseAuth mAuth;
    String verificationID;
    ProgressBar bar;
    TextView birth, signup, text, gender;


    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    Button selectImageBtn, uploadImageBtn;
    private ImageView image;

    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        btnverify = findViewById(R.id.btnverifyOTP);
        btngenOTP = findViewById(R.id.btngenerateOTP);
        mAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.bar);
        text = findViewById(R.id.text);
        radiogender = findViewById(R.id.radiogender);

        fullname = findViewById(R.id.name);
        signupbtn = findViewById(R.id.signupbtn);
        initDatePicker();
        dateButton = findViewById(R.id.birth);
        dateButton.setText(getTodayDate());

        signup = findViewById(R.id.Signup);
        birth = findViewById(R.id.textbirth);
        gender = findViewById(R.id.gender);
        female = findViewById(R.id.female);
        male = findViewById(R.id.male);


        selectImageBtn = findViewById(R.id.selectImageButton);
        image = findViewById(R.id.firebaseImage);








        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thegender ="";
                if(male.isChecked())
                    thegender="male";
                else
                    thegender="female";
                String name = fullname.getText().toString();
                String date = getTodayDate();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("date", date);
                hashMap.put("gender", thegender);
                String phone1 = phone.getText().toString();
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("uploading file...");
                progressDialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("image/" + phone1);
                storageReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                image.setImageURI(null);
                                Toast.makeText(Login.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                FirebaseFirestore.getInstance().collection("Users")
                        .document("" + phone1)
                        .set(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Date saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });


        btngenOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkphone = phone.getText().toString();

                if (TextUtils.isEmpty(checkphone) || checkphone.length() != 10 || !TextUtils.isDigitsOnly(checkphone))
                    Toast.makeText(Login.this, "Enter Valid Phone", Toast.LENGTH_SHORT).show();
                else {
                    String number = phone.getText().toString();
                    bar.setVisibility(View.VISIBLE);
                    sendverificationcode(number);
                    otp.setVisibility(View.VISIBLE);
                    btnverify.setVisibility(View.VISIBLE);
                }

            }
        });
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp.getText().toString()))
                    Toast.makeText(Login.this, "Enter Valid Phone", Toast.LENGTH_SHORT).show();
                else {
                    verifycode(otp.getText().toString());
                }
            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            image.setImageURI(imageUri);

        }
    }




    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, "Verifictaion Faild", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(Login.this, "Code sent", Toast.LENGTH_SHORT);
            btnverify.setEnabled(true);
            bar.setVisibility(View.INVISIBLE);
        }
    };


    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
        signinbyCredentials(credential);

    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = task.getResult().getUser();
                    long creationTimestamp = user.getMetadata().getCreationTimestamp();
                    long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
                    if (creationTimestamp == lastSignInTimestamp) {
                        //do create new user

                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT);
                        phone.setVisibility(View.INVISIBLE);
                        otp.setVisibility(View.INVISIBLE);
                        btnverify.setVisibility(View.INVISIBLE);
                        btngenOTP.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.INVISIBLE);
                        gender.setVisibility(View.VISIBLE);
                        signupbtn.setVisibility(View.VISIBLE);
                        fullname.setVisibility(View.VISIBLE);
                        dateButton.setVisibility(View.VISIBLE);
                        radiogender.setVisibility(View.VISIBLE);
                        birth.setVisibility(View.VISIBLE);
                        signup.setVisibility(View.VISIBLE);
                        male.setVisibility(View.VISIBLE);
                        female.setVisibility(View.VISIBLE);
                        selectImageBtn.setVisibility(View.VISIBLE);
                    } else {
                        //user is exists, just do login
                        Toast.makeText(Login.this, "already", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));

                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
        });

    }
 // אם כבר התחברת מהטלפון אז זה מתחבר ישר
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            startActivity( new Intent(Login.this , MainActivity.class));
            finish();
      }
    }


    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "PEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}


