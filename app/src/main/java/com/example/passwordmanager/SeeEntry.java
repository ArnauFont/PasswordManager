package com.example.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SeeEntry extends AppCompatActivity {

    private TextInputEditText etWebSite, etUsername, etPassword;
    private Button buttonCopyUsename, buttonCopyPassword;

    private MenuItem itemEdit, itemSave;

    private boolean isEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_entry);

        setup();
        setOnClickListeners();
        getInfoDataBase();

        if(savedInstanceState == null){
            isEditing = false;
        }
    }

    private void setup() {
        etWebSite = findViewById(R.id.etSeeWebSite);
        etUsername = findViewById(R.id.etSeeUsername);
        etPassword = findViewById(R.id.etSeePassword);

        buttonCopyPassword = findViewById(R.id.bCopyPassword);
        buttonCopyUsename = findViewById(R.id.bCopyUsername);

        itemEdit = findViewById(R.id.action_edit);
        itemSave = findViewById(R.id.action_save);

        Toolbar toolbar = findViewById(R.id.toolbarSeeEntry);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setOnClickListeners() {

        buttonCopyUsename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                if (!username.equals("")) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("textToCopy", username);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(SeeEntry.this, "Usuario copiado al portapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCopyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                if (!password.equals("")) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("textToCopy", password);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(SeeEntry.this, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getInfoDataBase() {
        Intent intent = getIntent();
        String webSite = intent.getStringExtra("webSite");

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from passwordManager where webSite = '" + webSite + "'", null);
        if (cursor.moveToNext()) {
            etWebSite.setText(cursor.getString(0));
            etUsername.setText(cursor.getString(1));
            etPassword.setText(cursor.getString(2));
        } else {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            finish();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Toast.makeText(this, "oncreateoptionsmenu ejecutado", Toast.LENGTH_SHORT).show();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);

        itemEdit = menu.findItem(R.id.action_edit);
        itemSave = menu.findItem(R.id.action_save);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            etUsername.setFocusable(true);
            etUsername.setFocusableInTouchMode(true);

            etPassword.setFocusableInTouchMode(true);
            etPassword.setFocusable(true);

            showUserIsAbleToEdit();

            itemSave.setVisible(true);
            itemEdit.setVisible(false);


        } else if (item.getItemId() == R.id.action_save) {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
            SQLiteDatabase database = admin.getWritableDatabase();

            String webSite = etWebSite.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if(!webSite.equals("") && !username.equals("") && !password.equals("")) {

                ContentValues cv = new ContentValues();
                cv.put("webSite", webSite);
                cv.put("username", username);
                cv.put("password", password);
                database.update("passwordManager", cv, "webSite = '" + webSite + "'", null);
                showUserCommit();
            }
            else{
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
            database.close();

        }

        else if (item.getItemId() == R.id.action_delete) {

            String webSite = etWebSite.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(SeeEntry.this);
            builder.setTitle("ELIMINAR ENTRADA");
            builder.setMessage("Seguro que quiere eliminar la entrada de '" + webSite + "'?");
            builder.setCancelable(false);
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(SeeEntry.this, "BD", null, 1);
                    SQLiteDatabase database = admin.getWritableDatabase();

                    database.delete("passwordManager", "webSite = '" + webSite + "'", null);

                    database.close();
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
        return super.onOptionsItemSelected(item);
    }

    private void showUserIsAbleToEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SeeEntry.this);
        builder.setTitle("ENTRADA EDITABLE");
        builder.setMessage("Ahora ya puede editar el usuario y la contraseña");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserCommit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SeeEntry.this);
        builder.setTitle("ENTRADA MODIFICADA");
        builder.setMessage("El usuario y contraseña han sido modificados correctamente");
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
}