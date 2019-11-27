package com.scullyapps.recipebook

import android.app.AlertDialog
import android.app.Dialog
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.data.Ingredient
import com.scullyapps.recipebook.data.Recipe
import com.scullyapps.recipebook.prompts.IngredientDialog
import com.scullyapps.recipebook.prompts.RatingDialog
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_open_recipe.*
import kotlinx.android.synthetic.main.dialog_rating.*

// this class is used to rate, delete or edit an activity.

class OpenRecipe : AppCompatActivity() {


    var rating = 0.0f
    var edited = false

    var ingredients = arrayListOf<Ingredient>()

    lateinit var recipe : Recipe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_recipe)

        supportActionBar?.hide()

        rating_bar.setIsIndicator(true)

        recipe = intent.extras?.getParcelable("recipe")!!

        if(recipe == null) {
            Log.e("OpenRecipe", "How have we not got a parcel? Something is terrible broken!")
            System.exit(1)
        }

        recipe_name.text = recipe.name
        rating_bar.rating = recipe.rating.toFloat()
        text_instructions.text.insert(0, recipe.description)



        setClickListener()

    }


    fun loadIngredients() {

        val projection = arrayOf(
            Contract.INGREDIENTS._ID,
            Contract.INGREDIENTS.NAME
        )

        val c = contentResolver.query(Contract.URECIPE, projection, null, null, null)
    }

    fun applyRating(r : Float) {

        rating = r

        // if we're modifying the rating, then we must let the activity know we've updated details
        if(rating_bar.rating != r)
            setSaveable()

        // change our class-wide rating variable, and the actual view
        rating_bar.rating = r
    }

    fun setSaveable() {
        if(makeRecipe() != recipe) {
            edited = true
            save_recipe.isEnabled = true
        } else {
            edited = false
            save_recipe.isEnabled = false
        }
    }


    fun setClickListener() {

        val context = this

        // rating bar inherits from a AbsSeekBar, which utilizes a touchlistener
        // found: https://stackoverflow.com/a/4010379

        rating_bar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                val dialog = RatingDialog(context, "Pot Noodle", rating_bar.rating)

                dialog.ok.setOnClickListener {
                    applyRating(dialog.r.rating)
                    dialog.cancel()
                }

                dialog.show()

                return v?.onTouchEvent(event) ?: true
            }
        })

        add_ingredient.setOnClickListener {
            val dialog = IngredientDialog(this)

            dialog.ok.setOnClickListener {
                print("Name of Ingredient: ${dialog.getName()}")
                print("Amnt of Ingredient: ${dialog.getAmt()}")
            }

            dialog.show()
        }
    }

    fun makeRecipe() : Recipe {
        return Recipe(recipe.id, recipe.name, text_instructions.text.toString(), this.rating.toInt())
    }

}



