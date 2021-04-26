package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private ImageView ivBackground;
    private String PASSWORD = "passwordCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivBackground = findViewById(R.id.imageBackground);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        ivBackground.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(hasCreatedPassword()){
                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                else {
                    Intent i = new Intent(MainActivity.this, CreatePasswordActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 1300);

    }

    private boolean hasCreatedPassword(){
        SharedPreferences sharedPreferences = getSharedPreferences(PASSWORD, MODE_PRIVATE);
        return !(sharedPreferences.getString(PASSWORD, "").equals(""));
    }

    @Override
    public void onBackPressed(){

    }
}