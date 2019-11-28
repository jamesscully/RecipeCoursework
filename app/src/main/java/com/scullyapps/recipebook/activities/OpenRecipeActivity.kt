package com.scullyapps.recipebook.activities

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.R
import com.scullyapps.recipebook.data.Contract
import com.scullyapps.recipebook.data.DBHelper
import com.scullyapps.recipebook.dialogs.IngredientDialog
import com.scullyapps.recipebook.dialogs.RatingDialog
import com.scullyapps.recipebook.models.Ingredient
import com.scullyapps.recipebook.models.Recipe
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_open_recipe.*

// this class is used to rate, delete or edit an activity.

class OpenRecipeActivity : AppCompatActivity() {

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

        setDefaultValues()

        setListeners()
        loadIngredients()

    }

    fun save() {
        val cv = ContentValues()

        // get our entered values and store them
        cv.put("name", recipe_name.text.toString())
        cv.put("rating", rating_bar.rating.toInt())
        cv.put("instructions", text_instructions.text.toString())

        contentResolver.update(Contract.ALL_RECIPES, cv, "_id=?", arrayOf("${recipe.id}"))

        // ingredients need to be added separately
        for(s in newIngredients)
            addNewIngredient(s)

        // since we've just saved, re-new the recipe and disable our save button
        recipe = makeRecipe()

        // reload our ingredients - this function removes and adds again
        loadIngredients()

        checkSaveState()

    }

    fun delete() {
        contentResolver.delete(Contract.ALL_RECIPES, "${recipe.id}", null)
        finish()
    }

    fun loadIngredients() {

        // remove temp newIngredients, and views
        newIngredients.clear()
        layout_ingredients.removeAllViews()

        val projection = arrayOf(
            Contract.INGREDIENTS._ID,
            Contract.INGREDIENTS.NAME
        )
        val c = contentResolver.query(Contract.REC_ING_TABLE, projection, recipe.id.toString(), null, null)

        if(c == null) return

        c.moveToFirst()

        // reload ingredients
        while(!c.isAfterLast) {

            val id = c.getInt(2); val name = c.getString(3)
            val ingredient = Ingredient(id, name)

            ingredients.add(ingredient)

            val view = IngredientView(this, name, "", id)

            view.btnRemove.setOnClickListener {
                contentResolver.delete(Contract.REC_ING_TABLE, id.toString(), null)
                view.visibility = View.GONE
                edited = true
                checkSaveState()
            }

            layout_ingredients.addView(view)

            c.moveToNext()
        }
    }

    fun addNewIngredient(name : String) {
        val db = DBHelper(this)
        val c : Cursor? = db.doesIngredExist(name)

        val cv = ContentValues()

        // the recipe id will never change, thus we can put it in the cv
        cv.put("recipe_id", recipe.id)

        // if the ingredient exists, get its id, else create an entry
        if(c != null) {
            c.moveToFirst()

            val ingId = c.getInt(0)
            cv.put("ingredient_id", ingId)
            c.close()

        } else {

            val insCv = ContentValues()
            insCv.put("ingredientname", name)

            contentResolver.insert(Contract.ALL_INGREDIENTS, insCv)

            // we'll need the newly created ingredients ID
            val newId = db.getIdByName(name)

            if(newId == -1) {
                throw IllegalAccessException("newId was not set")
            }

            cv.put("ingredient_id", newId)
        }

        // finally, make an association between the recipe and ingred.
        contentResolver.insert(Contract.REC_ING_TABLE, cv)
    }

    /*
         #############################

         THERE IS NO CONTENT-PROVIDER CODE BELOW HERE
         There's a lot of logic behind functionality, i.e. not showing save button until edit has occurred, or Dialog code below this line

         #############################
    */



    // If we have changes, then see if the user wants to save them
    override fun onBackPressed() {
        if(edited) {
            val build = AlertDialog.Builder(this)
            build.setMessage("You have unsaved changes, do you wish to save them?")
            build.setPositiveButton("Save", { dialogInterface, i ->
                save()
                super.onBackPressed()
            })
            build.setNegativeButton("No, exit", { dialogInterface, i ->
                super.onBackPressed()
            })

            build.show()

        } else {
            super.onBackPressed()
        }


    }


    // loads recipe data into views
    fun setDefaultValues() {
        recipe_name.text.insert(0, recipe.name)
        rating_bar.rating = recipe.rating.toFloat()
        text_instructions.text.insert(0, recipe.description)
    }



    // sets our rating bar and checks if modified
    fun applyRating(r : Float) {
        rating_bar.rating = r
        checkSaveState()
    }



    // if we've edited/added anything, show/hide the save button
    fun checkSaveState() {
        edited = (makeRecipe() != recipe || newIngredients.size > 0)
        save_recipe.isEnabled = edited
    }


    fun setListeners() {



        val txtChangeListener : TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkSaveState()
            }
        }

        // use the above to check if we've modified
        text_instructions.addTextChangedListener(txtChangeListener)
        recipe_name.addTextChangedListener(txtChangeListener)


        btn_delete.setOnClickListener {
            val build = AlertDialog.Builder(this)

                build.setMessage("Are you sure you wish to delete ${name}?")
                build.setPositiveButton("Delete", { dialogInterface, i ->
                    delete()
                    dialogInterface.cancel()
                })

                build.setNegativeButton("No", { dialogInterface, i ->  dialogInterface.cancel()})
                build.setCancelable(true)

            build.show()

        }

        val context = this

        // rating bar inherits from an AbsSeekBar, which utilizes a touchlistener
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

                val view = IngredientView(this, dialog.getName(), "")

                view.btnRemove.setOnClickListener {
                    newIngredients.remove(dialog.getName())
                    view.visibility = View.GONE
                    edited = true
                    checkSaveState()
                }

                layout_ingredients.addView(view)

                checkSaveState()
                dialog.cancel()
            }

            dialog.show()
        }

        save_recipe.setOnClickListener { save() }
    }

    // make a recipe object from our inputs
    fun makeRecipe() : Recipe {
        return Recipe(recipe.id,
                      recipe_name.text.toString(),
                      text_instructions.text.toString(),
                      rating_bar.rating.toInt())
    }
}