package com.example.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import javax.security.auth.login.LoginException;

public class LogInActivity extends AppCompatActivity {

    private String PASSWORD = "passwordCode";
    private String KEY_WORD = "achantaCampeon";
    Button button, buttonPassRecover;

    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        textInputPassword = findViewById(R.id.etLogIn);
        button = findViewById(R.id.buttonLogIn);
        buttonPassRecover = findViewById(R.id.buttonPasswordFotgotten);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = textInputPassword.getEditText().getText().toString();
                if(!pass.equals("")) {
                    SharedPreferences sharedPreferences = getSharedPreferences(PASSWORD, MODE_PRIVATE);
                    String passCorrect = sharedPreferences.getString(PASSWORD, "");
                    if (pass.equals(passCorrect)) {
                        Intent i = new Intent(LogInActivity.this, Home.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                    else{
                        Toast.makeText(LogInActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LogInActivity.this, "Escriba la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPassRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = textInputPassword.getEditText().getText().toString();
                if(word.equals(KEY_WORD)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                    builder.setTitle("CAMBIAR CONTRASEÑA");
                    builder.setMessage("Seguro que quieres cambiar tu contraseña?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(LogInActivity.this, CreatePasswordActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });
    }
}