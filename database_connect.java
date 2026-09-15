package com.example.myapplication1.db;

import com.example.myapplication1.MainActivity;

public class database_connect extends MainActivity {
    private database_connect(){
        //to prevent instantiation
    }

     public static class tables{

        public static final String TABLE_NAME = "pantry_items";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_EXPIRY = "expiry_date";
     }

     public static class RecipeTab {
         public static final String TABLE_NAME = "recipes";
         public static final String COLUMN_ID = "id";
         public static final String COLUMN_NAME = "name";
         public static final String COLUMN_INSTRUCTIONS = "instruction";
     }

    public static class IngredientsTab{

        public static final String TABLE_NAME = "ingredients_recipe";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_REQUIRED_QUANTITY = "required_quantity";
        public static final String COLUMN_UNIT = "unit";
    }

    //settings table
    public static class SettingsTab{

        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_EXPIRY_ALERTS = "expiry_alerts";
    }
}
