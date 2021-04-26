package com.example.passwordmanager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class FragmentCreatePassword extends Fragment {

    private TextInputLayout etGeneratePassword;
    private TextInputEditText text;
    private SeekBar seekBar;
    private TextView tvPasswordCharaters;
    Button buttonGenerate, buttonCopy;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_create_password_menu, container, false);

        etGeneratePassword = v.findViewById(R.id.etGeneratePassword);
        seekBar = v.findViewById(R.id.seekBar);
        tvPasswordCharaters = v.findViewById(R.id.passwordCharacters);
        buttonGenerate = v.findViewById(R.id.buttonGeneratePassword);
        buttonCopy = v.findViewById(R.id.buttonCopyPassword);
        text = v.findViewById(R.id.edittext);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPasswordCharaters.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                String password = etGeneratePassword.getEditText().getText().toString();
                if(!password.equals("")) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("textToCopy", password);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(getContext(), "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                int length = seekBar.getProgress();

                text.setText(generatePassword(length));
            }
        });

        return v;
    }

    public String generatePassword(int length){
        StringBuilder passBuilder = new StringBuilder(length);
        Random random = new Random();

        if(length>0){
            for(int i = 0; i < length; ++i){
                int a = random.nextInt(4);
                switch (a){
                    case 0:
                        passBuilder.append(LOWER.charAt(random.nextInt(LOWER.length())));
                        break;
                    case 1:
                        passBuilder.append(UPPER.charAt(random.nextInt(UPPER.length())));
                        break;
                    case 2:
                        passBuilder.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
                        break;
                    case 3:
                        passBuilder.append(PUNCTUATION.charAt(random.nextInt(PUNCTUATION.length())));
                        break;
                }
            }

            return new String(passBuilder);
        }

        Toast.makeText(getContext(), "El tamaño de la contraseña debe ser mas grande que 0", Toast.LENGTH_SHORT).show();
        return "";
    }

    private void hideKeyBoard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

