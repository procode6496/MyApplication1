
package com.example.myapplication1.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.myapplication1.R;
import com.example.myapplication1.activity.RecipeDetails;
import com.example.myapplication1.activity.Recipe;

import java.util.List;

public class AdapterRecipe extends RecyclerView.Adapter<AdapterRecipe.RecipeViewHolder> {

    private final Context context;
    private List<Recipe> recipes;

    public AdapterRecipe(Context context, List<Recipe> recipes) { // Constructor
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (position < 0 || position >= recipes.size()) {
            return;
        }
        Recipe recipe = recipes.get(position);
        if (recipe == null) {
            holder.textRecipeName.setText("Unknown Recipe");
            holder.textIngredientCount.setText("0 ingredients");
            holder.itemView.setOnClickListener(null);
            return;
        }

        String name = recipe.getName();
        holder.textRecipeName.setText(name == null || name.trim().isEmpty() ? "Unnamed recipe" : name);

        int ingredientCount = 0;
        if (recipe.getIngredients() != null) {
            ingredientCount = recipe.getIngredients().size();
        }
        holder.textIngredientCount.setText(ingredientCount + " ingredients available");

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RecipeDetails.class);
            intent.putExtra(RecipeDetails.EXTRA_RECIPE_ID, recipe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView textRecipeName;
        final TextView textIngredientCount;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textIngredientCount = itemView.findViewById(R.id.textIngredientCount);
        }
    }
}