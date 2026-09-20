package com.example.myapplication1.db;

import com.example.myapplication1.db.pantry;
import com.example.myapplication1.db.Recipe;
import com.example.myapplication1.db.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class IngredientsMatcher {

    private  IngredientsMatcher(){
        /*This is a utility class, where i check whether or not the user
        *can make a recipe, and check the rules
        * that ingredient exist in the pantry, have enough quantity and compatible unit*/
    }

    public static  boolean canMakeRecipe(Recipe recipe, List<pantry> pantries){

        if (recipe == null || pantries == null || pantries.isEmpty()){
            return false;
        }

        List<RecipeIngredient> requiredIngredients = recipe.getIngredients();
        if (requiredIngredients == null || requiredIngredients.isEmpty()){
            return false;
        }

        for (RecipeIngredient required: requiredIngredients){
            if(required == null){
                return false;
            }
            boolean ingredientPass = false;
            for (pantry pantry1: pantries) {
                if (pantry1 == null) {
                    continue;
                }
                if (!sameIngredient(required.getIngredientName(), pantry1.getName())){
                    continue;
                }
                if (!compatibleUnits(required.getUnit(), pantry1.getUnit())) {
                    continue;
                }
                double pantry1Quantity = convertToBaseUnit(pantry1.getQuantity(), pantry1.getUnit());
                double requiredQuantity = convertToBaseUnit(required.getRequiredQuantity(), required.getUnit());

                if (pantry1Quantity >= requiredQuantity) {
                    ingredientPass = true;
                    break;
                }
            }
            if (!ingredientPass) {
                return false;
            }
        }
        return true;
    }

    public  static  List<Recipe> getSuggestedRecipes(List<Recipe> recipes, List<pantry> pantries){

        List<Recipe> suggestions = new ArrayList<>();
        if(recipes == null || pantries == null){
            return  suggestions;
        }

        for (Recipe recipe: recipes){
            if(canMakeRecipe(recipe, pantries)){
                suggestions.add(recipe);
            }
        }
        return  suggestions;
    }

    private  static  boolean sameIngredient(String first, String second){
        if(first == null || second == null){
            return  false;
        }

        return normalizeName(first).equals(normalizeName(second));
    }

    private static String normalizeName(String name){
        if (name == null){
            return "";
        }
        String value = name.trim ().toLowerCase(Locale.ROOT);
        if(value.isEmpty()){
            return value;
        }
        //remove common plural forms
        if(value.endsWith("ies") && value.length() > 3){
            return value.substring(0, value.length() - 3) + "y";

        }
        if(value.endsWith("oes")&& value.length() > 3){
            return  value.substring(0, value.length() -2);
        }
        if(value.endsWith("es") && value.length() > 2){
            return value.substring(0, value.length() -2);
        }
        if(value.endsWith("s") && value.length() > 1){
            return value.substring(0, value.length() -1);
        }

        return  value;
    }

    private  static  boolean compatibleUnits(String requiredUnit, String pantryUnit){
        //here we check whether units can be compared

        if (requiredUnit == null || pantryUnit == null){
            return  false;
        }
        String required = normaliseUnit(requiredUnit);
        String pantry1 = normaliseUnit(pantryUnit);

        if (required.equals(pantry1)){
            return  true;
        }

        if (isWeightUnit(required) && isWeightUnit(pantry1)){
            return  true;
        }
        if (isVolumeUnit(required) && isVolumeUnit(pantry1)){
            return true;
        }
        if (isPieceUnit(required) && isPieceUnit(pantry1)){
            return  true;
        }
        return  false;
    }
    private  static String normaliseUnit(String unit){
        return  unit.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean isWeightUnit(String unit){
        return unit.equals("g") || unit.equals("gram") || unit.equals("grams")
                || unit.equals("kg") || unit.equals("Kilogram") || unit.equals("kilograms");
    }
    private static boolean isVolumeUnit(String unit){
        return unit.equals("ml") || unit.equals("millilitre") || unit.equals("millilitres")
                || unit.equals("milliliter") || unit.equals("milliliters") || unit.equals("l")
                || unit.equals("litre") || unit.equals("litres") || unit.equals("liter") || unit.equals("liters");
    }
    private static  boolean isPieceUnit(String unit){
        return  unit.equals("piece") || unit.equals("pieces") || unit.equals("unit") || unit.equals("units");
    }
    private static double convertToBaseUnit(double quantity, String unit){
        if (unit == null){
            return  quantity;
        }
        String normalized = normaliseUnit(unit);

        switch (normalized){
            case "kg":
            case "Kilogram":
            case "kilograms":
                return quantity * 1000.0;

            case "g":
            case "gram":
            case "grams":
                return  quantity;

            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return  quantity * 1000.0;

            case "ml":
            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
                return quantity;

            case "piece":
            case "pieces":
            case "unit":
            case "units":
            default:
                return quantity;
        }
    }
}
