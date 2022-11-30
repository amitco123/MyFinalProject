package com.example.myfinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class UpdateFragment extends Fragment {

    HomeFragment homeFragment= new HomeFragment();
    public RadioGroup rg,rg1;
    RadioButton rb,rb1;
    EditText neck,age,height,weight,waist;
    int Neck1,Age1,Height1,Weight1,Waist1;
    double fatpresenthge;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        neck = view.findViewById(R.id.neck1);
        age = view.findViewById(R.id.age1);
        height = view.findViewById(R.id.height1);
        weight = view.findViewById(R.id.weight1);
        waist = view.findViewById(R.id.waist1);

        rg = view.findViewById(R.id.radiotype);
        rg.setOnCheckedChangeListener(onCheckedChangeListener);
        rg1 = view.findViewById(R.id.radiogender);
        rg1.setOnCheckedChangeListener(onCheckedChangeListener);

        Button button= view.findViewById(R.id.update1);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Age1 = Integer.parseInt(age.getText().toString());
                Height1 = Integer.parseInt(height.getText().toString());
                Weight1 = Integer.parseInt(weight.getText().toString());
                Neck1 = Integer.parseInt(neck.getText().toString());
                Waist1 = Integer.parseInt(waist.getText().toString());

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.mass:
                    // Write your code here
                    break;
                case R.id.hitov:
                    // Write your code here
                    break;
                default:
                    break;
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.male:
                    // Write your code here
                    break;
                case R.id.female:
                    // Write your code here
                    break;
                default:
                    break;
            }
        }
    };

}

