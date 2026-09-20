
package com.example.myapplication1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class helper extends SQLiteOpenHelper {

    public helper(Context context) {
        super(context, database_connect.DATABASE_NAME, null, database_connect.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPantryTable(db);
        createRecipeTable(db);
        createIngredientsTable(db);
        createSettingsTable(db);
        seedRecipe(db);
        seedSettings(db);
    }

    private void createPantryTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + database_connect.tables.TABLE_NAME + " (" +
                database_connect.tables.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                database_connect.tables.COLUMN_NAME + " TEXT NOT NULL, " +
                database_connect.tables.COLUMN_QUANTITY + " REAL NOT NULL, " +
                database_connect.tables.COLUMN_UNIT + " TEXT NOT NULL, " +
                database_connect.tables.COLUMN_EXPIRY + " TEXT)";
        db.execSQL(sql);
    }

    private void createRecipeTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + database_connect.RecipeTab.TABLE_NAME + " (" +
                database_connect.RecipeTab.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                database_connect.RecipeTab.COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                database_connect.RecipeTab.COLUMN_INSTRUCTIONS + " TEXT NOT NULL)";
        db.execSQL(sql);
    }

    private void createIngredientsTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + database_connect.IngredientsTab.TABLE_NAME + " (" +
                database_connect.IngredientsTab.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                database_connect.IngredientsTab.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY + " REAL NOT NULL, " +
                database_connect.IngredientsTab.COLUMN_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + database_connect.IngredientsTab.COLUMN_RECIPE_ID + ") REFERENCES " +
                database_connect.RecipeTab.TABLE_NAME + "(" + database_connect.RecipeTab.COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(sql);
    }

    private void createSettingsTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + database_connect.SettingsTab.TABLE_NAME + " (" +
                database_connect.SettingsTab.COLUMN_KEY + " TEXT PRIMARY KEY, " +
                database_connect.SettingsTab.COLUMN_VALUE + " TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + database_connect.IngredientsTab.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + database_connect.RecipeTab.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + database_connect.tables.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + database_connect.SettingsTab.TABLE_NAME);
        onCreate(db);
    }

    public long insertAPantry(pantry item) {
        if (item == null) return -1L;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(database_connect.tables.COLUMN_NAME, item.getName());
        values.put(database_connect.tables.COLUMN_QUANTITY, item.getQuantity());
        values.put(database_connect.tables.COLUMN_UNIT, item.getUnit());
        if (item.getExpiryD() != null && !item.getExpiryD().trim().isEmpty())
            values.put(database_connect.tables.COLUMN_EXPIRY, item.getExpiryD());
        else
            values.putNull(database_connect.tables.COLUMN_EXPIRY);
        return db.insert(database_connect.tables.TABLE_NAME, null, values);
    }

    public List<pantry> getAllPantry() {
        List<pantry> pantryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.tables.TABLE_NAME, null, null, null, null, null,
                database_connect.tables.COLUMN_NAME + " COLLATE NOCASE ASC");
        try {
            while (cursor.moveToNext()) {
                pantry item = cursorToPantry(cursor);
                if (item != null) pantryList.add(item);
            }
        } finally { cursor.close(); }
        return pantryList;
    }

    public pantry getPantryByID(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.tables.TABLE_NAME, null,
                database_connect.tables.COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        try {
            if (cursor.moveToFirst()) return cursorToPantry(cursor);
        } finally { cursor.close(); }
        return null;
    }

    public int updatePantry(pantry item) {
        if (item == null || item.getId() <= 0) return 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(database_connect.tables.COLUMN_NAME, item.getName());
        values.put(database_connect.tables.COLUMN_QUANTITY, item.getQuantity());
        values.put(database_connect.tables.COLUMN_UNIT, item.getUnit());
        if (item.getExpiryD() != null && !item.getExpiryD().trim().isEmpty())
            values.put(database_connect.tables.COLUMN_EXPIRY, item.getExpiryD());
        else
            values.putNull(database_connect.tables.COLUMN_EXPIRY);
        return db.update(database_connect.tables.TABLE_NAME, values,
                database_connect.tables.COLUMN_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    public int delPantry(long id) {
        return getWritableDatabase().delete(database_connect.tables.TABLE_NAME,
                database_connect.tables.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllPantry() {
        getWritableDatabase().delete(database_connect.tables.TABLE_NAME, null, null);
    }

    private pantry cursorToPantry(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(database_connect.tables.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(database_connect.tables.COLUMN_NAME);
        int quantityIndex = cursor.getColumnIndex(database_connect.tables.COLUMN_QUANTITY);
        int unitIndex = cursor.getColumnIndex(database_connect.tables.COLUMN_UNIT);
        int expiryIndex = cursor.getColumnIndex(database_connect.tables.COLUMN_EXPIRY);
        if (idIndex < 0 || nameIndex < 0 || quantityIndex < 0 || unitIndex < 0) return null;
        pantry item = new pantry();
        item.setId(cursor.getLong(idIndex));
        item.setName(cursor.getString(nameIndex));
        item.setQuantity(cursor.getDouble(quantityIndex));
        item.setUnit(cursor.getString(unitIndex));
        if (expiryIndex >= 0 && !cursor.isNull(expiryIndex)) item.setExpiryD(cursor.getString(expiryIndex));
        return item;
    }

    private void seedRecipe(SQLiteDatabase db) {
        insertRecipes(db, "Omelete with veggies", "Beat eggs and add chopped veggies on a bowl, the fry till fully set",
                new IngredientD[]{new IngredientD("Eggs",4,"Pieces"),new IngredientD("Tomatoes",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Carrots",1,"Piece")});
        insertRecipes(db, "Chicken and Rice Stew", "Wash and boil rice and set asside, then fry chicken with anions and papers, then add tomatoes and prefferable vagies, then combine with Rice",
                new IngredientD[]{new IngredientD("Chicken",250,"g"),new IngredientD("Rice",200,"g"),new IngredientD("Onion",1,"Piece"),new IngredientD("Tomatoes",2,"Piece")});
        insertRecipes(db, "Tomato Pasta", "Boil pasta, keep aside, prepare the tomato sauce with onion and garlic, then tomatoes",
                new IngredientD[]{new IngredientD("Pasta",250,"g"),new IngredientD("Tomatoes",4,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Garlic",2,"Pieces")});
        insertRecipes(db, "Garlic Toast Bread", "Mix Garlic with Butter and spread it over bread, toast it until golden brown",
                new IngredientD[]{new IngredientD("Bread",2,"Pieces"),new IngredientD("Butter",20,"g"),new IngredientD("Garlic",1,"Piece")});
        insertRecipes(db, "Cheese SandWich", "Place cheese on the bread slices and toast until golden brown",
                new IngredientD[]{new IngredientD("Cheese",50,"g"),new IngredientD("Bread",2,"Pieces")});
        insertRecipes(db, "PanCakes", "Mix flour, milk, eggs, and sugar. Add baking powder, cook portions in a hot pan util golden.",
                new IngredientD[]{new IngredientD("Flour",200,"g"),new IngredientD("Eggs",2,"Units"),new IngredientD("Milk",250,"ml"),new IngredientD("Sugar",30,"g"),new IngredientD("Baking Powder",5,"g")});
        insertRecipes(db, "Sphatlo/Kota", "Fry potato chips till chisp, fry polony and viean, cut the bred in half, take out a piece of bread on the inside, add the chips on the hole and polony and vien on top of the bread and add cheese slice, and put back the bread you took out on top.",
                new IngredientD[]{new IngredientD("Eggs",1,"Piece"),new IngredientD("Bread",200,"g"),new IngredientD("Polony",200,"g"),new IngredientD("Russian",1,"Piece"),new IngredientD("Cheese",20,"g"),new IngredientD("Potatoes Chips",200,"g")});
        insertRecipes(db, "Beef  Okra Stew & Pap", "Fry the beef with onions and papers, then add tomatoes and boil after 35min add okra the boil for another 20min, then boil water and add meal meal stir till desired consistency",
                new IngredientD[]{new IngredientD("Beef",250,"g"),new IngredientD("Tomatoes",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Papers",2,"Pieces"),new IngredientD("Meal Meal",200,"g"),new IngredientD("Okra",200,"g")});
        insertRecipes(db, "Veggies broth", "Boil all desired veggitables for about 45minutes till smooth",
                new IngredientD[]{new IngredientD("Potatoes",2,"Pieces"),new IngredientD("Carrots",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Papers",3,"Pieces"),new IngredientD("Brokoly",2,"Pieces"),new IngredientD("Pies",30,"g")});
        insertRecipes(db, "Chicken Feets & Pap", "Fry chicken feets with onion and papers, then add tomatoes let it boil then add soup and let it simmer, Boil water and add meal meal while stirring till desired consistency.",
                new IngredientD[]{new IngredientD("Chicken Feets",250,"g"),new IngredientD("Tomatoes",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Papers",1,"Piece"),new IngredientD("Meal Meal",200,"g")});
        insertRecipes(db, "Mangwinya and Mince", "Mix Flour with water, salt, sugar, yeast, let it rest then fry minces with onion, papers, tomatos, peas and carrot, then fry the fat cakes the golden",
                new IngredientD[]{new IngredientD("Mince",200,"g"),new IngredientD("Tomatoes",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("Carrots",2,"Pieces"),new IngredientD("Peas",30,"g"),new IngredientD("Salt",5,"g"),new IngredientD("Yeast",5,"g"),new IngredientD("Flour",250,"g")});
        insertRecipes(db, "Fish $ Chips", "Remove bones from hake, cut it in half's prepare flour with spies and add egg to it with small water, dip the fish then fry till golden, the fry potatoes chips til chrisp.",
                new IngredientD[]{new IngredientD("Hake Fish",250,"g"),new IngredientD("Flour",20,"g"),new IngredientD("Potato Chips",250,"g"),new IngredientD("Eggs",1,"Piece")});
        insertRecipes(db, "Beef  Stir Fry", "Slice beefs into stripes, then fry with garlic and onion, then add veggies like papers",
                new IngredientD[]{new IngredientD("Beef",250,"g"),new IngredientD("Garlic",2,"Pieces"),new IngredientD("Onion",1,"Piece"),new IngredientD("papers",3,"Pieces")});
        insertRecipes(db, "Maccaroni and Cheese", "Boil the maccaroni, rinsine the add cheese and bit of desired soup/source",
                new IngredientD[]{new IngredientD("Maccaroni",250,"g"),new IngredientD("Cheese",100,"g"),new IngredientD("Sauce",35,"g")});
        insertRecipes(db, "Uphuthu & Amasi", "Boil water then add meal meal, keep staring till disered rice consitency structure, then add amasi while serving",
                new IngredientD[]{new IngredientD("Meal Meal",200,"g"),new IngredientD("Amaasi",1,"l")});
    }

    private void insertRecipes(SQLiteDatabase db, String name, String instructions, IngredientD[] ingredients) {
        ContentValues values = new ContentValues();
        values.put(database_connect.RecipeTab.COLUMN_NAME, name);
        values.put(database_connect.RecipeTab.COLUMN_INSTRUCTIONS, instructions);
        long recipeId = db.insert(database_connect.RecipeTab.TABLE_NAME, null, values);
        if (recipeId == -1L || ingredients == null) return;
        for (IngredientD ingredient : ingredients) {
            if (ingredient == null) continue;
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(database_connect.IngredientsTab.COLUMN_RECIPE_ID, recipeId);
            ingredientValues.put(database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME, ingredient.name);
            ingredientValues.put(database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY, ingredient.quantity);
            ingredientValues.put(database_connect.IngredientsTab.COLUMN_UNIT, ingredient.unit);
            db.insert(database_connect.IngredientsTab.TABLE_NAME, null, ingredientValues);
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.RecipeTab.TABLE_NAME, null, null, null, null, null,
                database_connect.RecipeTab.COLUMN_NAME + " COLLATE NOCASE ASC");
        try {
            while (cursor.moveToNext()) {
                Recipe recipe = cursorToRecipe(cursor);
                if (recipe != null) {
                    recipe.setIngredients(getRecipeIngredients(recipe.getId()));
                    recipes.add(recipe);
                }
            }
        } finally { cursor.close(); }
        return recipes;
    }

    public Recipe getRecipeById(long recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.RecipeTab.TABLE_NAME, null,
                database_connect.RecipeTab.COLUMN_ID + "=?", new String[]{String.valueOf(recipeId)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                Recipe recipe = cursorToRecipe(cursor);
                if (recipe != null) recipe.setIngredients(getRecipeIngredients(recipeId));
                return recipe;
            }
        } finally { cursor.close(); }
        return null;
    }

    public List<RecipeIngredient> getRecipeIngredients(long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.IngredientsTab.TABLE_NAME, null,
                database_connect.IngredientsTab.COLUMN_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)},
                null, null, database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME + " COLLATE NOCASE ASC");
        try {
            while (cursor.moveToNext()) {
                RecipeIngredient ingredient = cursorToRecipeIngredient(cursor);
                if (ingredient != null) ingredients.add(ingredient);
            }
        } finally { cursor.close(); }
        return ingredients;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_ID)));
        recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_NAME)));
        recipe.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(database_connect.RecipeTab.COLUMN_INSTRUCTIONS)));
        return recipe;
    }

    private RecipeIngredient cursorToRecipeIngredient(Cursor cursor) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setId(cursor.getLong(cursor.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_ID)));
        ingredient.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_RECIPE_ID)));
        ingredient.setIngredientName(cursor.getString(cursor.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_INGREDIENT_NAME)));
        ingredient.setRequiredQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_REQUIRED_QUANTITY)));
        ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(database_connect.IngredientsTab.COLUMN_UNIT)));
        return ingredient;
    }

    private void seedSettings(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(database_connect.SettingsTab.COLUMN_KEY, "expiry_alerts");
        values.put(database_connect.SettingsTab.COLUMN_VALUE, "true");
        db.insert(database_connect.SettingsTab.TABLE_NAME, null, values);
    }

    public boolean getExpiryAlertsEnable() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(database_connect.SettingsTab.TABLE_NAME,
                new String[]{database_connect.SettingsTab.COLUMN_VALUE},
                database_connect.SettingsTab.COLUMN_KEY + "=?", new String[]{"expiry_alerts"}, null, null, null);
        try {
            if (cursor.moveToFirst()) return Boolean.parseBoolean(cursor.getString(0));
        } finally { cursor.close(); }
        return true;
    }

    public boolean setExpiryAlertsEnabled(boolean enabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(database_connect.SettingsTab.COLUMN_VALUE, String.valueOf(enabled));
        int rows = db.update(database_connect.SettingsTab.TABLE_NAME, values,
                database_connect.SettingsTab.COLUMN_KEY + "=?", new String[]{"expiry_alerts"});
        return rows > 0;
    }

    private static class IngredientD {
        String name;
        double quantity;
        String unit;
        IngredientD(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}
