package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.ETC1;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class CreatePasswordActivity extends AppCompatActivity {

    private TextInputLayout etPassword;
    private String PASSWORD = "passwordCode";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        etPassword = findViewById(R.id.etCeatePassword);
        button = findViewById(R.id.buttonCreatePassword);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pswrd = etPassword.getEditText().getText().toString();
                if(!pswrd.equals("")){
                    SharedPreferences settings = getSharedPreferences(PASSWORD, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PASSWORD, pswrd);
                    editor.apply();

                    Intent i = new Intent(CreatePasswordActivity.this, Home.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                else{
                    Toast.makeText(CreatePasswordActivity.this, "Introduce la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}