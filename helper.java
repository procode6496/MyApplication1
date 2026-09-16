package com.example.myapplication1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myapplication1.db.RecipeIngredient;
import com.example.myapplication1.db.Recipe;
import com.example.myapplication1.db.pantry;

import java.util.ArrayList;
import java.util.List;
public class helper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;
    public helper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = String.format("CREATE TABLE%s(%sINTEGER PRIMARY KEY AUTOINCREMENT, %sTEXT NOT NULL, %sREAL NOT NULL, %sTEXT NOT NULL,%sTEXT );",
                database_connect.tables.TABLE_NAME,
                database_connect.tables.COLUMN_ID,
                database_connect.tables.COLUMN_NAME,
                database_connect.tables.COLUMN_QUANTITY,
                database_connect.tables.COLUMN_UNIT,
                database_connect.tables.COLUMN_EXPIRY);
        db.execSQL(createPantryTable);

        String createRecipeTable = String.format("CREATE TABLE%s(%sINTEGER PRIMARY KEY AUTOINCREMENT,%sTEXT NOT NULL,%sTEXT NOT NULL ); ",
                database_connect.RecipeTab.TABLE_NAME,
                database_connect.RecipeTab.COLUMN_ID,
                database_connect.RecipeTab.COLUMN_NAME,
                database_connect.RecipeTab.COLUMN_INSTRUCTIONS);
        db.execSQL(createRecipeTable);

        String createIngredientsTable = String.format("CREATE TABLE%s(%sINTEGER PRIMARY KEY AUTOINCREMENT,%sINTEGER NOT NULL,%sTEXT NOT NULL,%sREAL NOT NULL,%sTEXT NOT NULL, FOREIGN KEY (%s) REFERENCES %s(%s))",
                database_connect.IngredientsTab.TABLE_NAME,
                database_connect.IngredientsTab.COLUMN_ID,
                database_connect.IngredientsTab.COLUMN_RECIPE_ID,
                database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME,
                database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY,
                database_connect.IngredientsTab.COLUMN_UNIT,
                database_connect.IngredientsTab.COLUMN_RECIPE_ID,
                database_connect.RecipeTab.TABLE_NAME,
                database_connect.RecipeTab.COLUMN_ID);
        db.execSQL(createIngredientsTable);


        String createSettingsTable = String.format("CREATE TABLE%s(%sINTEGER PRIMARY KEY AUTOINCREMENT,%sINTEGER NOT NULL DEFAULT 1)",
                database_connect.SettingsTab.TABLE_NAME,
                database_connect.SettingsTab.COLUMN_ID,
                database_connect.SettingsTab.COLUMN_EXPIRY_ALERTS);
        db.execSQL(createSettingsTable);

        seedRecipe(db);
        ContentValues settings = new ContentValues();
        settings.put(database_connect.SettingsTab.COLUMN_EXPIRY_ALERTS, 1);
        db.insert(database_connect.SettingsTab.TABLE_NAME, null, settings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +
                database_connect.IngredientsTab.TABLE_NAME
        );

        db.execSQL("DROP TABLE IF EXISTS " +
                database_connect.RecipeTab.TABLE_NAME
        );

        db.execSQL("DROP TABLE IF EXISTS " +
                database_connect.tables.TABLE_NAME
        );

        db.execSQL("DROP TABLE IF EXISTS " +
                database_connect.SettingsTab.TABLE_NAME
        );

        onCreate(db);
    }

    //creating the pantry CRUD Operations
    public long insertAPantry(pantry item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(database_connect.tables.COLUMN_NAME,
                item.getName()
        );
        values.put(database_connect.tables.COLUMN_QUANTITY,
                item.getQuantity()
        );
        values.put(database_connect.tables.COLUMN_UNIT,
                item.getUnit()
        );
        values.put(database_connect.tables.COLUMN_EXPIRY,
                item.getExpiryD()
        );
        return db.insert(database_connect.tables.TABLE_NAME, null, values);
    }

    public List<pantry>getAllPantry(){
        List<pantry> pantry1 = new ArrayList();
        SQLiteDatabase db  = getReadableDatabase();
        Cursor cur = db.query(database_connect.tables.TABLE_NAME,
                null, null, null, null, null,
                database_connect.tables.COLUMN_NAME + "ASC");
        if (cur.moveToFirst()) {
            do {
                pantry item1 = new pantry();
                item1.setId(cur.getInt(cur.getColumnIndexOrThrow(
                        database_connect.tables.COLUMN_ID
                )));
                item1.setName(cur.getString(cur.getColumnIndexOrThrow(
                        database_connect.tables.COLUMN_NAME
                )));
                item1.setQuantity(cur.getDouble(cur.getColumnIndexOrThrow(
                        database_connect.tables.COLUMN_QUANTITY
                )));
                item1.setUnit(cur.getString(cur.getColumnIndexOrThrow(
                        database_connect.tables.COLUMN_UNIT
                )));
                item1.setExpiryD(cur.getString(cur.getColumnIndexOrThrow(
                        database_connect.tables.COLUMN_EXPIRY
                )));

                pantry1.add(item1);
            }
            //setting a condition to check the cursor movement
            while (cur.moveToNext());
        }
        cur.close();
        return pantry1;
    }

    public pantry getPantry(int id){
        SQLiteDatabase base1 = getReadableDatabase();

        Cursor cus = base1.query(database_connect.tables.TABLE_NAME, null,
                database_connect.tables.COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(id)
                }, null, null, null);

        pantry item2 = null;
        if (cus.moveToFirst()) {
            item2 = new pantry();
            item2.setId(id);
            item2.setName(cus.getString(cus.getColumnIndexOrThrow(
                    database_connect.tables.COLUMN_NAME
            )));
            item2.setQuantity(cus.getDouble(cus.getColumnIndexOrThrow(
                    database_connect.tables.COLUMN_QUANTITY
            )));
            item2.setUnit(cus.getString(cus.getColumnIndexOrThrow(
                    database_connect.tables.COLUMN_UNIT
            )));
            item2.setExpiryD(cus.getString(cus.getColumnIndexOrThrow(
                    database_connect.tables.COLUMN_EXPIRY
            )));
        }
        cus.close();
        return item2;
    }

    public int updatePantry(pantry pantry2) {
        SQLiteDatabase base2 = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(database_connect.tables.COLUMN_NAME, pantry2.getName());
        value.put(database_connect.tables.COLUMN_QUANTITY, pantry2.getQuantity());
        value.put(database_connect.tables.COLUMN_UNIT, pantry2.getUnit());
        value.put(database_connect.tables.COLUMN_EXPIRY, pantry2.getExpiryD());

        return base2.update(
                database_connect.tables.COLUMN_NAME, value,
                database_connect.tables.COLUMN_ID + "=?",

                new String[]{
                        String.valueOf(pantry2.getId())
                }
        );
    }

    //deleting a recipe
    public int delPantry(int id){
        SQLiteDatabase base3 = getWritableDatabase();

        return base3.delete(
                database_connect.tables.COLUMN_NAME,
                database_connect.tables.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    //inserting a recipe
    public long insertRecipe(Recipe recipe){
        SQLiteDatabase base4 = getWritableDatabase();
        ContentValues valu = new ContentValues();

        valu.put(database_connect.RecipeTab.COLUMN_NAME, recipe.getName());
        valu.put(database_connect.RecipeTab.COLUMN_INSTRUCTIONS, recipe.getInstructions());

        long recipeId = base4.insert(
                database_connect.RecipeTab.TABLE_NAME, null, valu
        );

        if (recipeId != -1 && recipe.getIngredients() != null){
            for(RecipeIngredient ingredient : recipe.getIngredients()){
                insertRecipeIngredient(base4, recipeId, ingredient);
            }
        }
        return recipeId;
    }

    private void insertRecipeIngredient(SQLiteDatabase base4, long recipeId, RecipeIngredient ingredient) {
        ContentValues cont = new ContentValues();

        cont.put(database_connect.IngredientsTab.COLUMN_RECIPE_ID, recipeId);
        cont.put(database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME, ingredient.getIngredientName());
        cont.put(database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY, ingredient.getRequiredQuantity());
        cont.put(database_connect.IngredientsTab.COLUMN_UNIT, ingredient.getUnit());

        base4.insert(database_connect.RecipeTab.TABLE_NAME, null, cont);
    }

    //creating recipes
    private void seedRecipe(SQLiteDatabase db){
        insertRecipes(db, "Omelete with veggies",
                "Beat eggs and add chopped veggies on a bowl, the fry till fully set",
                new String[][]{
                        {"Eggs", "4", "Pieces"},   {"Tomatoes", "2", "Pieces"},
                        {"Onion", "1", "Piece"},   {"Carrots", "1", "Piece"},
                });
        //keep adding more recipes
    }

    private void insertRecipes(SQLiteDatabase db, String name, String instructions, String[][] ingredients) {
        ContentValues vals = new ContentValues();

        vals.put(database_connect.RecipeTab.COLUMN_NAME, name);
        vals.put(database_connect.RecipeTab.COLUMN_INSTRUCTIONS, instructions);
        long recipeId = db.insert(database_connect.RecipeTab.TABLE_NAME, null, vals);

        if (recipeId == -1){
            return;
        }
        for (String[] ingredient : ingredients){
            ContentValues con = new ContentValues();

            con.put(database_connect.IngredientsTab.COLUMN_RECIPE_ID, recipeId);
            con.put(database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME, ingredient[0]);
            con.put(database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY, ingredient[1]);
            con.put(database_connect.IngredientsTab.COLUMN_UNIT, ingredient[2]);
            db.insert(database_connect.IngredientsTab.TABLE_NAME, null, con);
        }

    }

    public List<Recipe> getAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cus = db.query(database_connect.RecipeTab.COLUMN_NAME, null, null, null, null, null,
                database_connect.RecipeTab.COLUMN_NAME + "ASC");

        if (cus.moveToFirst()){
            do{
                Recipe recipe = new Recipe();
                recipe.setId(cus.getInt(cus.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_ID)));
                recipe.setName(cus.getString(cus.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_NAME)));
                recipe.setIngredients(getIngredientsForRecipe(recipe.getId()));
                recipes.add(recipe);
            }while(cus.moveToNext());
        }
        cus.close();
        return recipes;
    }

    public Recipe getRecipeById(int recipeId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(database_connect.RecipeTab.TABLE_NAME, null,
                database_connect.RecipeTab.COLUMN_ID + "=?", new String[]{
                        String.valueOf(recipeId)
                }, null, null, null
        );

        Recipe recipe = null;
        if (cur.moveToFirst()){
            recipe = new Recipe();
            recipe.setId(cur.getInt(cur.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_ID)));
            recipe.setName(cur.getString(cur.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_INSTRUCTIONS)));
            recipe.setIngredients(getIngredientsForRecipe(recipeId));
        }

        cur.close();
        return recipe;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId){
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cus = database.query(database_connect.IngredientsTab.TABLE_NAME, null,
                database_connect.IngredientsTab.COLUMN_RECIPE_ID + "=?", new String[]{
                        String.valueOf(recipeId)
                }, null, null, database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME + "ASC"
        );

        if (cus.moveToFirst()){
            do{
                RecipeIngredient ingredient = new RecipeIngredient();

                ingredient.setId(cus.getInt(cus.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_ID)));
                ingredient.setIngredientName(cus.getString(cus.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME)));
                ingredient.setRequiredQuantity(cus.getDouble(cus.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY)));
                ingredient.setUnit(cus.getString(cus.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_UNIT)));

                ingredients.add(ingredient);
            }while(cus.moveToNext());
        }
        cus.close();
        return  ingredients;
    }

}
