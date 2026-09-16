package com.example.myapplication1.db;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.db.helper;
import com.example.myapplication1.db.pantry;
import com.example.myapplication1.R;

public class add_edit extends AppCompatActivity{

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    private EditText editName;
    private EditText editQuantity;
    private EditText editExpiryD;
    private Spinner spinnerUnit;
    private Button saveB;
    private TextView  screenTitle;
    private helper dbHelper;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);

        dbHelper = new helper(this);
        initializeViews();
        setupUnitSpinner();
        checkEditMode();

        saveB.setOnClickListener(view -> saveTngredient());
    }

    private void checkEditMode() {
        if (getIntent().hasExtra(EXTRA_ITEM_ID)){
            itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);

            if (itemId != -1) {
                screenTitle.setText("Edit Ingredient");
                saveB.setText("Update Ingredient");

            }loadIngredient(itemId);
        }else{
            screenTitle.setText("Add Ingredient");
            saveB.setText("Save Ingredient");
        }
    }

    private void loadIngredient(int id) {

        pantry item = dbHelper.getPantry(id);
        if(item ==null){
            Toast.makeText(this, "Ingredient could not be found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        editName.setText(item.getName());
        editQuantity.setText(String.valueOf(item.getQuantity()));
        editExpiryD.setText(item.getExpiryD());
    }

    private void setupUnitSpinner() {
        String[] units = {
                "Pieces", "g", "kg", "ml", "l"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
    }

    private void initializeViews() {
        editName = findViewById(R.id.editIgredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiryD = findViewById(R.id.editEpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        saveB = findViewById(R.id.buttonSave);
        screenTitle = findViewById(R.id.textScreenTitle);

    }
    private void selectUnit(String unit) {
        if (unit == null) {
            return;
        }

        ArrayAdapter adapter = (ArrayAdapter) spinnerUnit.getAdapter();

        int position = adapter.getPosition(unit);
        if (position >= 0) {
            spinnerUnit.setSelection(position);
        }
    }

    private  void saveTngredient(){
        String name = editName.getText().toString().trim();
        String quantity = editQuantity.getText().toString().trim();
        String expiry = editExpiryD.getText().toString().trim();
        String unit = String.valueOf(spinnerUnit.getSelectedItemId());

        if( !validateInput(name, quantity)){
            return;
        }
        double quant;
        try{
            quant = Double.parseDouble(quantity);
        }catch(NumberFormatException e){
            editQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();

            return;
        }

        pantry item = new pantry(name, quant, unit, expiry);
        if(itemId == -1){
            long result = dbHelper.insertAPantry(item);
            if(result != -1){
                Toast.makeText(this, "Ingredient added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Failed to add ingredient.", Toast.LENGTH_SHORT).show();
            }
        }else{
            item.setId(itemId);
            int result = dbHelper.updatePantry(item);

            if (result > 0){
                Toast.makeText(this, "Ingredient updated Successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Failed to update ingredient", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //input validation
    private  boolean validateInput(String name, String quantity){
        if(TextUtils.isEmpty(name)){
            editName.setError("Ingredient name is required");
            editName.requestFocus();
            return false;

        }
        if(TextUtils.isEmpty(quantity)){
            editQuantity.setError("Quantity is required!");
            editQuantity.requestFocus();
            return false;
        }
        return false;
    }

}
