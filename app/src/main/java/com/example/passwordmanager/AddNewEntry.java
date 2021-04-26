package com.example.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewEntry extends AppCompatActivity {

    private TextInputLayout etWebSite, etUsername, etPassword;
    private TextInputEditText textPassword;
    Button buttonSave, buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        setUp();
        setListeners();

        setTitle("Add new user");


    }

    private void setUp(){
        etWebSite = findViewById(R.id.etWebSite);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        textPassword = findViewById(R.id.editTextAddNewEntry);

        buttonCreate = findViewById(R.id.buttonCreatePasswordAdd);
        buttonSave = findViewById(R.id.buttonSaveNewEntry);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String webSite = etWebSite.getEditText().getText().toString();
                String username = etUsername.getEditText().getText().toString();
                String password = etPassword.getEditText().getText().toString();

                if(!webSite.equals("") && !username.equals("") && !password.equals("")) {

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(AddNewEntry.this, "BD", null, 1);
                    SQLiteDatabase database = admin.getWritableDatabase();

                    Cursor cursor = database.rawQuery("select * from passwordManager where webSite = '"+ webSite +"';", null);

                    if(cursor.moveToFirst()){
                        //Si ja esta a la base de dades

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEntry.this);
                        builder.setTitle("ENTRADA ENCONTRADA");
                        builder.setMessage("Ya se ha encontrado " + webSite + " en el registro, quiere modificar el usuario y/o la contraseña?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContentValues values = new ContentValues();
                                values.put("webSite", webSite);
                                values.put("username", username);
                                values.put("password", password);

                                database.update("passwordManager",  values, "webSite = '"+webSite+"'", null);

                                cursor.close();
                                database.close();

                                showUserCommit();

                            }
                        });
                        builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else{
                        //Si no esta a la base de dades
                        ContentValues values = new ContentValues();
                        values.put("webSite", webSite);
                        values.put("username", username);
                        values.put("password", password);

                        database.insert("passwordManager", null, values);

                        cursor.close();
                        database.close();

                        showUserCommit();
                    }

                }
                else{
                    Toast.makeText(AddNewEntry.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = 15;
                FragmentCreatePassword fragmentCreatePassword = new FragmentCreatePassword();
                String newPassword = fragmentCreatePassword.generatePassword(length);
                textPassword.setText(newPassword);
            }
        });
    }

    private void showUserCommit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEntry.this);
        builder.setTitle("ENTRADA AÑADIDA");
        builder.setMessage("El usuario y contraseña han sido añadidas correctamente");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}