package com.scullyapps.recipebook

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.data.Ingredient
import com.scullyapps.recipebook.data.Recipe
import com.scullyapps.recipebook.prompts.IngredientDialog
import com.scullyapps.recipebook.prompts.RatingDialog
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_open_recipe.*

// this class is used to rate, delete or edit an activity.

class OpenRecipe : AppCompatActivity() {


    var rating = 0.0f
    var edited = false

    var instructions = ""

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
        instructions = recipe.description


        setListeners()

        loadIngredients()

    }


    fun loadIngredients() {

        val projection = arrayOf(
            Contract.INGREDIENTS._ID,
            Contract.INGREDIENTS.NAME
        )

        val c = contentResolver.query(Contract.REC_ING_TABLE, projection, recipe.id.toString(), null, null)

        if(c == null) {
            return
        }

        c.moveToFirst()

        while(!c.isAfterLast) {
            ingredients.add(Ingredient(c.getInt(0), c.getString(3)))
            layout_ingredients.addView(IngredientView(this, c.getString(3), "Amount"))

            c.moveToNext()
        }
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


    fun setListeners() {

        val context = this


        text_instructions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                instructions = p0.toString()
                setSaveable()
            }
        })


        // rating bar inherits from a AbsSeekBar, which utilizes a touchlistener
        // found: https://stackoverflow.com/a/4010379

        rating_bar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val dialog = RatingDialog(context, recipe.name, rating_bar.rating)

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

        save_recipe.setOnClickListener {
            val cv = ContentValues(1)

            cv.put("name", recipe.name)
            cv.put("rating", rating.toInt())
            cv.put("instructions", text_instructions.text.toString())

            contentResolver.update(Contract.ALL_RECIPES, cv, "_id=?", arrayOf("${recipe.id}"))
        }
    }

    fun makeRecipe() : Recipe {
        return Recipe(recipe.id, recipe.name, text_instructions.text.toString(), this.rating.toInt())
    }

}



