    package com.example.myapplication1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication1.db.Adapter;
import com.example.myapplication1.db.SuggestedRecipes;
import com.example.myapplication1.db.Settings;
import com.example.myapplication1.db.add_edit;
import com.example.myapplication1.db.helper;
import com.example.myapplication1.db.pantry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  implements Adapter.OnpantryListener{

    private RecyclerView recyclePantry;
    private Adapter pAdapter;
    private helper dataBHelp;
    private TextView emptyPantry;
    private FloatingActionButton addIngredients;
    private final List<pantry> pantries = new ArrayList<>();
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dataBHelp = new helper(this);
        initializeViews();
        setupRecyclerV();
        setupListeners();
        setupBottomNavigation();
        loadpantry();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadpantry() {

        List<pantry> items = dataBHelp.getAllPantry();
        pantries.clear();
        pantries.addAll(items);
        pAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (pantries.isEmpty()){
            emptyPantry.setVisibility(View.VISIBLE);
            recyclePantry.setVisibility(View.GONE);
        }else{
            emptyPantry.setVisibility(View.GONE);
            recyclePantry.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        addIngredients.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, add_edit.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerV() {
        recyclePantry.setHasFixedSize(true);
        recyclePantry.setLayoutManager(new LinearLayoutManager(this));
        pAdapter = new Adapter(this, pantries, this);
        recyclePantry.setAdapter(pAdapter);
    }

    private void initializeViews() {
        recyclePantry = findViewById(R.id.recyclerViewPantry);
        emptyPantry =  findViewById(R.id.textEmptyPantry);
        addIngredients = findViewById(R.id.fabAddIngredient);
        bottomNav = findViewById(R.id.bottomNavigation);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (dataBHelp != null && pAdapter != null){
            loadpantry();
        }
    }

    @Override
    public void onEditClick(pantry item){
        Intent intent = new Intent(MainActivity.this, add_edit.class);
        intent.putExtra(add_edit.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }
    @Override
    public void onDeleteClick(pantry item){
        dataBHelp.delPantry(item.getId());
        loadpantry();
    }

    private void setupBottomNavigation(){
        bottomNav.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
            if (id == R.id.nav_pantry){
                return true;
            } else if (id == R.id.nav_recipes) {
                Intent intent = new Intent(MainActivity.this, SuggestedRecipes.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_pantry);
    }
}