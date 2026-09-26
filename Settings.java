package com.example.myapplication1.activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.myapplication1.R;
import com.example.myapplication1.db.helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Settings extends  AppCompatActivity{


    private SwitchCompat switchAlerts;
    private helper dbHelper;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        dbHelper = new helper(this);
        initializeViews();
        setupListener();
        loadSettings();
        setupBottomNavigation();
    }

    private void initializeViews() {
        switchAlerts = findViewById(R.id.switchAlerts);
        bottomNav = findViewById(R.id.bottomNavigation);
    }
    private  void setupListener() {
        switchAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                saveAlerts(isChecked));
    }
    private void loadSettings() {
        boolean enabled = dbHelper.getExpiryAlertsEnable();
        switchAlerts.setChecked(enabled);
    }
   void saveAlerts(boolean enable){
        boolean success = dbHelper.setExpiryAlertsEnabled(enable);
        if (success) {
            Toast.makeText(this, enable ? "Expiry alerts enabled" : "Expiry alerts disabled",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Could not save settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation(){

        bottomNav.setOnItemSelectedListener(item ->{
            int id = item.getItemId();

            if (id == R.id.nav_pantry){
                IntentHelper.open(Settings.this, MainActivity.class);
                return true;
            }else if (id == R.id.nav_recipes){
                return true;
            } else if (id == R.id.nav_settings) {
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_settings);
    }
    private  static  class IntentHelper{
        static  void open(android.content.Context context, Class<?> target) {
            Intent intent = new Intent(context, target);
            context.startActivity(intent);
        }
    }
}
