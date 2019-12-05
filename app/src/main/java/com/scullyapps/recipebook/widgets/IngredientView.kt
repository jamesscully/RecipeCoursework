package com.scullyapps.recipebook.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.scullyapps.recipebook.R
import kotlinx.android.synthetic.main.widget_ingredientview.view.*

class IngredientView(context: Context) : LinearLayout(context) {

    var btnRemove : ImageView
    var ingId : Int = -1

    constructor(context: Context, name : String, amt : String, id : Int = -1) : this(context) {
        ingredient_amount.text = amt
        ingredient_name.text = name
        ingId = id
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_ingredientview, this, true)

        btnRemove = ingred_remove
    }

    // this is used to allow the deletion of ingredients from
    // the ListIngredients activity - only if they have no assosciation to a recipe.
    fun isRemoveable(b : Boolean) {
        if(b)
            btnRemove.visibility = View.VISIBLE
        else
            btnRemove.visibility = View.GONE
    }


}