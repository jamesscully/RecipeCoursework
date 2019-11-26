package com.scullyapps.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_open_recipe.*

// this class is used to rate, delete or edit an activity.

class OpenRecipe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_recipe)

        supportActionBar?.hide()


        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))
        layout_ingredients.addView(IngredientView(this, "This is an ingredient", "1337g"))

    }
}
