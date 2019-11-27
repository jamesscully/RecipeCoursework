package com.scullyapps.recipebook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.prompts.RatingDialog
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_open_recipe.*
import kotlinx.android.synthetic.main.dialog_rating.*

// this class is used to rate, delete or edit an activity.

class OpenRecipe : AppCompatActivity() {


    var rating = 0.0f

    var edited = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_recipe)

        supportActionBar?.hide()

        rating_bar.setIsIndicator(true)
        rating_bar.rating = 5.0f



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

        setClickListener()

    }

    fun applyRating(r : Float) {


        // if we're modifying the rating, then we must let the activity know we've updated details
        if(rating_bar.rating != r)
            edited = true

        // change our class-wide rating variable, and the actual view
        rating = r
        rating_bar.rating = r
    }


    fun setClickListener() {

        val context = this

        // rating bar inherits from a AbsSeekBar, which utilizes a touchlistener
        // found: https://stackoverflow.com/a/4010379

        rating_bar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                val dialog = RatingDialog(context, "Pot Noodle", rating_bar.rating)

                dialog.ok.setOnClickListener {
                    rating_bar.rating = dialog.r.rating
                    dialog.cancel()
                }
                
                dialog.show()

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

}



