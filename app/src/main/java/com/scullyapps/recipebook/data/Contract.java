package com.scullyapps.recipebook.data;

import android.net.Uri;

public class Contract {

    public static final String AUTHORITY = "com.scullyapps.recipebook.data.RecipeCProvider";

    // location of database
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/databases/RecipeDB/");


    public static final Uri ALL_RECIPES     = Uri.parse("content://" + AUTHORITY + "/recipes");
    public static final Uri ALL_INGREDIENTS = Uri.parse("content://" + AUTHORITY + "/ingredients");
    public static final Uri REC_ING_TABLE   = Uri.parse("content://" + AUTHORITY + "/recipe_ingredients");

    public static Uri fromId(Uri uri, int id) {
        return Uri.parse(uri.toString() + "/" + id);
    }
    public static Uri fromId(Uri uri, long id) {
        return Uri.parse(uri.toString() + "/" + id);
    }

    public static class RECIPE {
        public static final String _ID          = "_id";
        public static final String NAME         = "name";
        public static final String INSTRUCTIONS = "instructions";
        public static final String RATING       = "rating";
    }

    public static class INGREDIENTS {
        public static final String _ID          = "_id";
        public static final String NAME         = "ingredientname";
    }

    public static class REC_ING {
        public static final String _RID          = "recipe_id";
        public static final String _CID          = "ingredient_id";
        public static final String NAME          = "name";
        public static final String INSTRUCTIONS  = "instructions";
        public static final String RATING        = "rating";
    }


}
