package com.scullyapps.recipebook.widgets

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.scullyapps.recipebook.R
import kotlinx.android.synthetic.main.widget_ingredientview.view.*

class IngredientView(context: Context) : LinearLayout(context) {
    private val TAG: String = "IngredientView";

    constructor(context: Context, name : String, amt : String) : this(context) {
        ingredient_amount.text = amt
        ingredient_name.text = name
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_ingredientview, this, true)
    }


}