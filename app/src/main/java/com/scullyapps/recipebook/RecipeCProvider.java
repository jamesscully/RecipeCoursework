package com.scullyapps.recipebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeCProvider extends ContentProvider {


    DBHelper helper;
    SQLiteDatabase db;

    private static final UriMatcher uriMatcher;

    public static final int CODE_RECIPES       = 1;
    public static final int CODE_RECIPE_ID     = 4;

    public static final int CODE_INGREDIENTS   = 2;
    public static final int CODE_INGREDIENT_ID = 5;


    public static final int CODE_REC_INGS      = 3;
    public static final int CODE_REC_INGS_ID   = 6;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "recipes", CODE_RECIPES);
        uriMatcher.addURI(Contract.AUTHORITY, "recipes/#", CODE_RECIPE_ID);

        uriMatcher.addURI(Contract.AUTHORITY, "ingredients", CODE_INGREDIENTS);
        uriMatcher.addURI(Contract.AUTHORITY, "ingredients/#", CODE_INGREDIENT_ID);

        uriMatcher.addURI(Contract.AUTHORITY, "recipe_ingredients", CODE_REC_INGS );
        uriMatcher.addURI(Contract.AUTHORITY, "recipe_ingredients/#", CODE_REC_INGS_ID);
    }


    // returns the Uri for a specific id
    public static Uri byID(Uri uri, int id) {
        return Uri.parse(uri.toString() + "/" + id);
    }




    @Override
    public boolean onCreate() {

        helper = new DBHelper(this.getContext());
        db = helper.getWritableDatabase();

        // if the DB is not null, then we've successfully created it.
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match = uriMatcher.match(uri);


        String last = uri.getLastPathSegment();


        switch (match) {
            case CODE_RECIPES:
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);

            case CODE_INGREDIENTS:
                return db.query("ingredients", projection, selection, selectionArgs, null, null, sortOrder );

            case CODE_REC_INGS :
                return db.rawQuery("SELECT r._id AS recipe_id, r.name, ri.ingredient_id, i.ingredientname FROM recipes r JOIN recipe_ingredients ri ON (r._id = ri.recipe_id) JOIN ingredients i ON (ri.ingredient_id = i._id) WHERE r._id == ?", new String[] { selection });

            case CODE_RECIPE_ID:
                return db.query("recipes", projection, "_id=" + last, null, null, null, sortOrder);

            case CODE_INGREDIENT_ID:
                return db.query("ingredients", projection, "_id=" + last, null, null, null, sortOrder);

        }



        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if(uri.getLastPathSegment() == null)
            return "vnd.android.cursor.dir/RecipeCProvider.data.text";
        else
            return "vnd.android.cursor.item/RecipeCProvider.data.text";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                return Contract.fromId(Contract.ALL_RECIPES, db.insert("recipes", null, contentValues));

            case CODE_INGREDIENTS:
                return Contract.fromId(Contract.ALL_INGREDIENTS, db.insert("ingredients", null, contentValues));

            case CODE_REC_INGS :
                db.insert("recipe_ingredients", null, contentValues);
                break;
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {


        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                db.delete("recipes", "_id=" + s, null);
                db.delete("recipe_ingredients", "recipe_id=" + s, null);
                break;
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        if(uriMatcher.match(uri) == CODE_RECIPES) {

            db.update("recipes", contentValues, s, strings);
            System.out.println("Updating recipes");

        }

        return 0;
    }
}
