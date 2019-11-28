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




    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "recipes", 1);
        uriMatcher.addURI(Contract.AUTHORITY, "recipes/#", 4);

        uriMatcher.addURI(Contract.AUTHORITY, "ingredients", 2);
        uriMatcher.addURI(Contract.AUTHORITY, "ingredients/#", 5);

        uriMatcher.addURI(Contract.AUTHORITY, "recipe_ingredients", 3);
        uriMatcher.addURI(Contract.AUTHORITY, "recipe_ingredients/#", 6);
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

        Log.d("CP", "Using uri: " + uri.getPath() + "match = " + match);

        switch (match) {
            case 1:
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
            case 2:
                return db.query("ingredients", projection, selection, selectionArgs, null, null, sortOrder );
            case 3:
                return db.rawQuery("select r._id as recipe_id, r.name, ri.ingredient_id, i.ingredientname "+"from recipes r "+"join recipe_ingredients ri on (r._id = ri.recipe_id)"+"join ingredients i on (ri.ingredient_id = i._id) where r._id == ?", new String[] { selection });
            case 4:
                String last = uri.getLastPathSegment();
                Log.d("DBHELP", last);
                break;
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        if(uriMatcher.match(uri) == 1) {

            db.update("recipes", contentValues, s, strings);
            System.out.println("Updating recipes");

        }

        return 0;
    }
}
