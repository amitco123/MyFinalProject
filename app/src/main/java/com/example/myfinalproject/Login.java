package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView textView= findViewById(R.id.signup);
        String text ="dont have account, register!";
        SpannableString ss =new SpannableString(text);

        button = findViewById(R.id.login_btn);
        ClickableSpan clickableSpan =new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        };
        ss.setSpan(clickableSpan,18, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void loginbt(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }


}
