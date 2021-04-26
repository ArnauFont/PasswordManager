package com.example.passwordmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FragmentMenu extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    ArrayList<String> myArray = new ArrayList<String>();

    private FloatingActionButton fabAddButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password_menu, container, false);

        getInfoDataBase();

        fabAddButton = v.findViewById(R.id.fabAddButton);
        listView = v.findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), SeeEntry.class);
                intent.putExtra("webSite", arrayAdapter.getItem(i));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),AddNewEntry.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfoDataBase();

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myArray);
        listView.setAdapter(arrayAdapter);
    }

    Comparator<String> stringComparator = new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
            return s.toLowerCase().compareTo(t1.toLowerCase());
        }
    };

    private void getInfoDataBase(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "BD", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        Cursor cursor = database.rawQuery("select webSite from passwordManager", null);
        cursor.moveToFirst();

        myArray = new ArrayList<>();

        while (!cursor.isAfterLast()){
            myArray.add(cursor.getString(0));
            cursor.moveToNext();
        }


        cursor.close();
        database.close();

        Collections.sort(myArray, stringComparator);
    }

    public void recieveQueryForListView(String query) {
        if(arrayAdapter != null) arrayAdapter.getFilter().filter(query);
    }

}
