package com.example.myapplication1.activity;

import android.content.Intent;
import android.os.Bundle;
import  android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;
import com.example.myapplication1.adapters.AdapterRecipe;
import com.example.myapplication1.db.helper;
import com.example.myapplication1.model.pantry;
import com.example.myapplication1.utils.IngredientsMatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipes extends AppCompatActivity{

    private  RecyclerView recyclerViewRecipe;
    private  TextView textNoRecipes;
    private  TextView textRecipeCount;
    private helper dbHelper;
    private AdapterRecipe adapterRecipe;
    private final List<Recipe> suggestedRecipes = new ArrayList<>();
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.suggested_recipes);

        dbHelper = new helper(this);
        initializeViews();
        setupRecyclerView();
        setupBottomNavigation();
        loadSuggestedRecipes();
    }

    private void initializeViews() {

        recyclerViewRecipe = findViewById(R.id.recyclerViewRecipes);
        textNoRecipes = findViewById(R.id.textNoRecipes);
        textRecipeCount = findViewById(R.id.textRecipeCount);
        bottomNav = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewRecipe.setLayoutManager(layoutManager);
        recyclerViewRecipe.setHasFixedSize(false);
        adapterRecipe = new AdapterRecipe(this, suggestedRecipes);
        recyclerViewRecipe.setAdapter(adapterRecipe);
    }


    private void loadSuggestedRecipes(){
        if (dbHelper == null || adapterRecipe == null){
            return;
        }
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<pantry> pantries = dbHelper.getAllPantry();
        List<Recipe> match = IngredientsMatcher.getSuggestedRecipes(allRecipes, pantries);
        suggestedRecipes.clear();
        suggestedRecipes.addAll(match);
        adapterRecipe.notifyDataSetChanged();
        updateScreen();
    }

    private void updateScreen() {
        int count = suggestedRecipes.size();
        textRecipeCount.setText(count + " recipe (s) available");
        if (count == 0) {
            recyclerViewRecipe.setVisibility(View.GONE);
            textNoRecipes.setVisibility(View.VISIBLE);
        }else{
            recyclerViewRecipe.setVisibility(View.VISIBLE);
            textNoRecipes.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (dbHelper != null && adapterRecipe != null) {
            loadSuggestedRecipes();
        }
    }

    private void setupBottomNavigation(){
        if(bottomNav == null){
            return;
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_pantry){
                Intent intent = new Intent(SuggestedRecipes.this, MainActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_recipes){
                return true;
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(SuggestedRecipes.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent); // Call startActivity BEFORE finish() to avoid window transitions bugs
                finish();
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_recipes);
    }

}
