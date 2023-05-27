package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Orderselection extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, textPlace , tvtimer1,date ;
    int t1Hour, t1Minute;
    private final static int REQUEST_CODE = 100;
    RadioGroup rgBtnGroupDescribe1, rgBtnGroupDescribe2, rgBtnGroupDescribe3, rgBtnGroupPlace, rgBtnGroupcheck;
    Spinner spinner;
    TextInputLayout textInputLayout;
    EditText loc1, loc;
    AutoCompleteTextView autoCompleteTextView;

    boolean flag;
    String sendaddress,Time,RdbToL,RdbFoS,RdbOoT,sendloc1,senddate,st9,sendhowmuchtime;
    private DatabaseReference databaseReference;
    Button continue1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderselection);

        date= findViewById(R.id.textView1);
        Intent takeit= getIntent();
        String st=takeit.getStringExtra("date");
        date.setText(st);
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");



        rgBtnGroupDescribe1 = findViewById(R.id.rgBtnGroupDescribe1);
        rgBtnGroupDescribe2 =  findViewById(R.id.rgBtnGroupDescribe2);
        rgBtnGroupDescribe3 =  findViewById(R.id.rgBtnGroupDescribe3);
        rgBtnGroupPlace =  findViewById(R.id.rgBtnGroupPlace);
        rgBtnGroupcheck =  findViewById(R.id.rgBtnGroupcheck);
        address =  findViewById(R.id.address);
        textPlace =  findViewById(R.id.textPlace);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Orderselection.this);
        textInputLayout =  findViewById(R.id.menu_drop);
        autoCompleteTextView =  findViewById(R.id.drop_items);
        tvtimer1=  findViewById(R.id.tv_timer1);
        spinner=  findViewById(R.id.spinner1);
        loc1=  findViewById(R.id.loc1);
        loc=  findViewById(R.id.loc);


        String[] items = {"Costume competition", "Getting ready for a date", "exhibition", "Doubles tennis"};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Orderselection.this,R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sendhowmuchtime=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Orderselection.this, sendhowmuchtime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(Orderselection.this, R.layout.item_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);



        tvtimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog= new TimePickerDialog(
                        Orderselection.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth ,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hour=hourOfDay;
                                t1Minute=minute;
                                String time=t1Hour +":"+ t1Minute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date= f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    tvtimer1.setText(f12Hours.format(date));
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        },12,0, true

                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                st9=adapterView.getItemAtPosition(i).toString();
            }
        });


        rgBtnGroupDescribe1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch (checkedId) {
                    case R.id.rbTalker:
                        RdbToL="Talker";
                        break;
                    case R.id.rbListener:
                        RdbToL="Listener";
                        break;
                }
            }
        });
        rgBtnGroupDescribe2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch (checkedId) {
                    case R.id.rbFunny: {
                        RdbFoS="Funny";

                    }
                    break;
                    case R.id.rbSerious: {
                        RdbFoS="Serious";
                    }
                    break;
                }
            }
        });
        rgBtnGroupDescribe3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch (checkedId) {
                    case R.id.rbOutgoing: {
                        RdbOoT="Outgoing";
                    }
                    break;
                    case R.id.rbTimid: {
                        RdbOoT="Timid";
                    }
                    break;
                }
            }
        });
        rgBtnGroupPlace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                sendaddress="2";
                switch (checkedId) {
                    case R.id.rbYour: {
                        address.setVisibility(View.VISIBLE);
                        loc.setVisibility(View.INVISIBLE);
                        getLastLocation();
                        sendaddress= address.getText().toString();
                    }
                    break;
                    case R.id.rbAnother: {
                        address.setVisibility(View.INVISIBLE);
                        loc.setVisibility(View.VISIBLE);
                        sendaddress=loc.getText().toString();
                    }
                    break;
                }
            }
        });

        rgBtnGroupcheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbYes: {
                        flag= true;
                        rgBtnGroupPlace.setVisibility(View.VISIBLE);
                        textPlace.setVisibility(View.VISIBLE);
                    }
                    break;
                    case R.id.rbNo: {
                        flag= false;
                        rgBtnGroupPlace.setVisibility(View.INVISIBLE);
                        textPlace.setVisibility(View.INVISIBLE);
                        address.setVisibility(View.INVISIBLE);
                        loc.setVisibility(View.INVISIBLE);
                    }
                    break;
                }
            }
        });

    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(Orderselection.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(Orderselection.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    address.setText(" address:" + addresses.get(0).getAddressLine(0));


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(Orderselection.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(Orderselection.this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }





    public void gopay(View view){

        Time=tvtimer1.getText().toString();
        sendloc1=loc1.getText().toString();
        senddate=date.getText().toString();
        if(flag==false){
            if (Time.equals("select time of meeting")||sendloc1.equals("") ||senddate.equals("")||st9.equals("") )
            {
                Toast.makeText(this, "something is missing", Toast.LENGTH_SHORT).show();
            }
            else{
                MyDatabaseHelper myDB = new MyDatabaseHelper(Orderselection.this);
                myDB.addBook(Time.trim(),
                        senddate.trim(),
                        st9.trim(),sendloc1.trim());
                //            Time, sendloc1 ,senddate,RdbOoT,RdbFoS,RdbToL,s9,sendhowmuchtime
                databaseReference.child(senddate).setValue("Catch");
                Intent go = new Intent(this, MainActivity.class);
                startActivity(go);
            }
        }
        else{
            if (Time.equals("select time of meeting")||sendloc1.equals("") ||senddate.equals("")||st9.equals("")||sendaddress.equals("") )
            {
                Toast.makeText(this, "something is missing", Toast.LENGTH_SHORT).show();
            }
            else{
                MyDatabaseHelper myDB = new MyDatabaseHelper(Orderselection.this);
                myDB.addBook(Time.trim(),
                        senddate.trim(),
                        st9.trim(),sendloc1.trim());
                databaseReference.child(senddate).setValue("Catch");
                //            Time, sendloc1 ,senddate,RdbOoT,RdbFoS,RdbToL,s9,sendhowmuchtime,sendaddress
                Intent go = new Intent(this, MainActivity.class);
                startActivity(go);
            }
        }
    }
}