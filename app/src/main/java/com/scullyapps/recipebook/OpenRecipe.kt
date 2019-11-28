package com.scullyapps.recipebook

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

    lateinit var recipe : Recipe

    var edited = false


    // these hold the data of the recipe stored within this activity, rather than directly modifying recipe

    var name = ""
    var instructions = ""
    var rating = 0.0f
    var ingredients = arrayListOf<Ingredient>()

    // this is used to mark ingredients that need to be added to the database.
    var newIngredients = arrayListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_recipe)

        supportActionBar?.hide()

        rating_bar.setIsIndicator(true)

        recipe = intent.extras?.getParcelable("recipe")!!

        if(recipe == null) {
            Log.e("OpenRecipe", "How have we not got a parcel? Something is terribly broken!")
            System.exit(1)
        }


        setDefaultValues()

        setListeners()
        loadIngredients()

    }

    // this loads the recipes values into local variables, and their needed views

    fun setDefaultValues() {

        recipe_name.text.insert(0, recipe.name)
        rating_bar.rating = recipe.rating.toFloat()
        text_instructions.text.insert(0, recipe.description)

        name = recipe.name
        instructions = recipe.description
        rating = recipe.rating.toFloat()
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
            checkSaveState()

        // change our class-wide rating variable, and the actual view
        rating_bar.rating = r
    }

    fun addNewIngredient(name : String) {
        val db = DBHelper(this)

        val c : Cursor? = db.doesIngredExist(name)


        val cv = ContentValues()

        // the recipe id will never change, thus we can put it in the cv
        cv.put("recipe_id", recipe.id)


        // we'll need to do some logic for the ingredient ID
        // either it already exists, so we just need to link it,
        // or we need to create a new ingredient and link it.

        var newId = -1;

        if(c != null) {
            c.moveToFirst()

            val ingId = c.getInt(0)
            cv.put("ingredient_id", ingId)

        } else {

            val insCv = ContentValues()
            insCv.put("ingredientname", name)

            contentResolver.insert(Contract.ALL_INGREDIENTS, insCv)

            // we'll need the newly created ingredients ID
            newId = db.getIdByName(name)

            if(newId == -1) {
                throw IllegalAccessException("newId was not set")
            }

            cv.put("ingredient_id", newId)
        }

        contentResolver.insert(Contract.REC_ING_TABLE, cv)
    }

    // if we've edited/added anything, show the save button
    fun checkSaveState() {
        if(makeRecipe() != recipe || newIngredients.size > 0) {
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
                checkSaveState()
            }
        })

        recipe_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = p0.toString()
                checkSaveState()
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
                newIngredients.add(dialog.getName())
                layout_ingredients.addView(IngredientView(this, dialog.getName(), ""))

                checkSaveState()
                dialog.cancel()
            }



            dialog.show()
        }

        save_recipe.setOnClickListener {
            val cv = ContentValues(1)

            cv.put("name", name)
            cv.put("rating", rating.toInt())
            cv.put("instructions", text_instructions.text.toString())

            contentResolver.update(Contract.ALL_RECIPES, cv, "_id=?", arrayOf("${recipe.id}"))


            for(s in newIngredients) {
                addNewIngredient(s)
            }


            // since we've just saved, re-new the recipe and disable our save button
            recipe = makeRecipe()

            checkSaveState()
        }
    }

    fun makeRecipe() : Recipe {
        return Recipe(recipe.id, name, text_instructions.text.toString(), this.rating.toInt())
    }

}



