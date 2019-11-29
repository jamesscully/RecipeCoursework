package com.scullyapps.recipebook.widgets

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.scullyapps.recipebook.R
import com.scullyapps.recipebook.models.Recipe
import kotlinx.android.synthetic.main.widget_recipeview.view.*

class RecipeView(context: Context, recipe : Recipe) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_recipeview, this, true)

        wd_recipe_name.text = recipe.name
        wd_recipe_rating.text = recipe.rating.toString()
    }

}