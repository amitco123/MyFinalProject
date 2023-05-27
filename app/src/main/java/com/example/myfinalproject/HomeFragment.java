package com.example.myfinalproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements SelectListener {
    View view;
    RecyclerView recyclerView;

    ImageView empty_imageview;
    TextView no_data;

    MyDatabaseHelper myDB;
    ArrayList<String> id, time, date, the_reason, address;
    CustomAdapter customAdapter;
    private DatabaseReference databaseReference;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home, container, false);
       recyclerView = view.findViewById(R.id.recyclerView);

       empty_imageview = view.findViewById(R.id.empty_imageview);
       no_data = view.findViewById(R.id.no_data);
       databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
       myDB = new MyDatabaseHelper(getActivity());
       id = new ArrayList<>();
       time = new ArrayList<>();
       date = new ArrayList<>();
       the_reason = new ArrayList<>();
       address = new ArrayList<>();


       customAdapter = new CustomAdapter (getActivity(),getContext(), id, time, date,
               the_reason,address,this);
       recyclerView.setAdapter(customAdapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       storeDataInArrays();


        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1){
//            recreate();
//        }
//    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        id.clear();
        time.clear();
        date.clear();
        the_reason.clear();
        address.clear();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                time.add(cursor.getString(1));
                date.add(cursor.getString(2));
                the_reason.add(cursor.getString(3));
                address.add(cursor.getString(4));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(String date, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete " + date + " ?");
        builder.setMessage("You are sure that you would like to cancel the meeting, the cancellation fee is 50 shekels " + date + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(getActivity());
                myDB.deleteOneRow(id);
                dialogInterface.dismiss();
                databaseReference.child(date).removeValue();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}