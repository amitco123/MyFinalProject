package com.example.myfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView BottomNavigationView;
    HomeFragment homeFragment= new HomeFragment();
    OrderFragment orderFragment =new OrderFragment();
    ReviewFragment reviewFragment =new ReviewFragment();
    UpdateFragment updateFragment=new UpdateFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView= findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        BottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                    return true;
                case R.id.add:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFragment).commit();
                    return true;
                case R.id.graph:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, reviewFragment).commit();
                    return true;

            }


                return false;
            }
        });
        }
        }

