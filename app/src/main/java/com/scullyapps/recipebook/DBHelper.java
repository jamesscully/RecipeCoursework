package com.scullyapps.recipebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "RecipesDB", null, 1);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean doesIngredExist(String ingredient) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE ingredientName=" + ingredient, null);

        if(c.getCount() > 0) {
            db.close();
            c.close();
            return true;
        }


        return false;
    }


}
