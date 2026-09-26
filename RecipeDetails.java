package com.example.myapplication1.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.db.helper;

public class RecipeDetails extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private helper dbHelper;
    private  TextView textRecipeName;
    private TextView textIngredients;
    private TextView textInstructions;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        dbHelper = new helper(this);
        initializeViews();
        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1L);

        if(recipeId <= 0){
           showErrorAndClose();
           return;
        }
        loadRecipe(recipeId);
    }

    private void loadRecipe(long recipeId) {
        Recipe recipe = dbHelper.getRecipeById(recipeId);
        if (recipe == null){
            showErrorAndClose();
            return;
        }
        textRecipeName.setText(recipe.getName() == null ? "Unnamed Recipe" : recipe.getName());
        StringBuilder ingredients = new StringBuilder();
        if (recipe.getIngredients() != null && ! recipe.getIngredients().isEmpty()) {

            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                ingredients.append(". ").append(ingredient.getIngredientName()).append("-")
                        .append(formatQuantity(ingredient.getRequiredQuantity())).
                        append(" ").append(ingredient.getUnit() == null ? "" : ingredient.getUnit())
                        .append("\n");
            }
        }else{
            ingredients.append("No ingredients listed.");
        }
        textIngredients.setText(ingredients.toString());
        String instructions = recipe.getInstructions();
        if (instructions == null || instructions.trim().isEmpty())
            textInstructions.setText("No instructions available.");
        else textInstructions.setText(instructions);
    }

    private void initializeViews(){
        textRecipeName = findViewById(R.id.textRecipeName);
        textIngredients = findViewById(R.id.textIngredients);
        textInstructions = findViewById(R.id.textInstructions);
    }
    private String formatQuantity(double quantity){
        if (quantity == (long) quantity){
            return String.valueOf((long) quantity);
        }

        return  String.valueOf(quantity);
    }

    private void showErrorAndClose(){
        Toast.makeText(this, "Recipe could not be found.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

}
