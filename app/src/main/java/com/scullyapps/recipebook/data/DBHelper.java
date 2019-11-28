package com.scullyapps.recipebook.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "RecipesDB", null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE recipes (" +
                               "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                               "name VARCHAR(128) NOT NULL, " +
                               "instructions VARCHAR(128) NOT NULL, " +
                               "rating INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE ingredients (" +
                               "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                               "ingredientname VARCHAR(128) NOT NULL" +
                               ");");

        sqLiteDatabase.execSQL("CREATE TABLE recipe_ingredients (" +
                                            "recipe_id INT NOT NULL, " +
                                            "ingredient_id INT NOT NULL," +
                                            "CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id)," +
                                            "CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id)," +
                                            "CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id));");

        addTestData(sqLiteDatabase);
    }

    public void addTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO recipes(name, instructions, rating) VALUES (\"Pasta\", \"Boil things\", 5)");
        db.execSQL("INSERT INTO recipes(name, instructions, rating) VALUES (\"Beef Burger\", \"Fry\", 2)");

        db.execSQL("INSERT INTO ingredients(ingredientname) VALUES (\"Salt\")");
        db.execSQL("INSERT INTO ingredients(ingredientname) VALUES (\"Beef Patty\")");

        db.execSQL("INSERT INTO recipe_ingredients(recipe_id, ingredient_id) VALUES (1,1)");
        db.execSQL("INSERT INTO recipe_ingredients(recipe_id, ingredient_id) VALUES (2,2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // if our version has changed, we'll want to renew the tables.
        if(i > i1) {
            sqLiteDatabase.execSQL("DROP TABLE recipes");
            sqLiteDatabase.execSQL("DROP TABLE ingredients");
            sqLiteDatabase.execSQL("DROP TABLE recipe_ingredients");

            onCreate(sqLiteDatabase);
        }
    }

    public int getIdByName(String name) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT _id FROM ingredients WHERE ingredientName=\"" + name + "\"", null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            return c.getInt(0);
        }

        return -1;
    }

    public boolean doesIngredAssociate(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM recipe_ingredients WHERE ingredient_id=\"" + id + "\"", null);

        boolean exists = c.getCount() > 0;

        c.close();

        return exists;

    }

    public Cursor doesIngredExist(String ingredient) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE ingredientName=\"" + ingredient + "\"", null);

        if(c.getCount() > 0) {
            return c;
        }
        return null;
    }


}
