package com.example.myapplication1.db;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> ingredients;

    public Recipe() {
        ingredients = new ArrayList<>();
    }

    public Recipe(int id, String name, String instructions){
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getInstructions(){
        return instructions;
    }
    public void setId(String instructions){
        this.instructions = instructions;
    }
    public List<RecipeIngredient> getIngredients(){
        return ingredients;
    }
    public void setIngredients(List<RecipeIngredient> ingredients){
        this.ingredients = ingredients;
    }
    public void addIngredient(RecipeIngredient ingredient){
        this.ingredients.add(ingredient);
    }
}
