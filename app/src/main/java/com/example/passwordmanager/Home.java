package com.example.passwordmanager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class Home extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    private FragmentMenu fragmentMenu;
    private FragmentCreatePassword fragmentCreatePassword;

    private HashMap<String, Boolean> menuSelector = new HashMap<>();
    private final String MENU = "fragmentMenu";
    private final String CREAR = "fragmentCrearContrasena";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentMenu = new FragmentMenu();
        fragmentCreatePassword = new FragmentCreatePassword();

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(Home.this, mDrawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        if (savedInstanceState == null) {
            menuSelector.put(MENU, true);
            menuSelector.put(CREAR, false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentMenu).commit();
            setTitle("Usuarios y Contraseñas");
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        fragmentMenu = new FragmentMenu();
        fragmentCreatePassword = new FragmentCreatePassword();

        switch (menuItem.getItemId()) {
            case R.id.menu_password:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    menuSelector.replace(MENU, true);
                    menuSelector.replace(CREAR, false);
                } else {
                    menuSelector.remove(MENU);
                    menuSelector.remove(CREAR);
                    menuSelector.put(MENU, true);
                    menuSelector.put(CREAR, false);
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentMenu).commit();
                //Toast.makeText(Home.this, "Menu password selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_crear_contrasena:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    menuSelector.replace(MENU, false);
                    menuSelector.replace(CREAR, true);
                } else {
                    menuSelector.remove(MENU);
                    menuSelector.remove(CREAR);
                    menuSelector.put(MENU, false);
                    menuSelector.put(CREAR, true);
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentCreatePassword).commit();
                //Toast.makeText(Home.this, "Menu create password selected", Toast.LENGTH_SHORT).show();
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

        manageSearchButton();
    }

    private void manageSearchButton() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (menuSelector.get(MENU) != null) {
            if (menuSelector.get(MENU)) {
                if (menu.findItem(R.id.action_search) == null) {
                    MenuItem item = menu.add(Menu.NONE, R.id.action_search, 5, "Search");
                }
            } else {
                menu.removeItem(R.id.action_search);
            }
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Toast.makeText(this, "oncreateoptionsmenu ejecutado", Toast.LENGTH_SHORT).show();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                fragmentMenu.recieveQueryForListView(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragmentMenu.recieveQueryForListView(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}