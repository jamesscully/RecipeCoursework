package com.scullyapps.recipebook.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.R
import com.scullyapps.recipebook.data.Contract
import com.scullyapps.recipebook.data.DBHelper
import com.scullyapps.recipebook.models.Recipe
import com.scullyapps.recipebook.widgets.RecipeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var recipes = arrayListOf<Recipe>()
    val db = DBHelper(this)


    var sort = "name DESC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // close the initial db, databases should be created.
        db.close()

        val intent = Intent(this, OpenRecipeActivity::class.java)

        btn_list_ingredients.setOnClickListener {
            startActivity(Intent(this, ListIngredientsActivity::class.java))
        }


        // when our spinner is changed, we'll need to call the sort function.
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                sortEntries()
            }
        }

        btn_new_recipe.setOnClickListener {

            // define our default values
            val name = "New Recipe"
            val inst = ""
            val rate = 1


            // load them into our ContentValues
            val cv = ContentValues()

            cv.put("name", name)
            cv.put("instructions", inst)
            cv.put("rating", rate)

            // this will get a recipe object directly from our new inserted recipe
            val recipe = Recipe.fromUri(contentResolver.insert(Contract.ALL_RECIPES, cv), contentResolver)

            intent.putExtra("recipe", recipe)
            intent.putExtra("new", true)

            startActivity(intent)
        }
    }

    fun sortEntries() {
        val mode = sortSpinner.selectedItem.toString()

        // naturally, we'll go with A-Z and High - Low as the sort options
        if(mode == "Title")
            sort = "name ASC"
        else
            sort = "rating DESC"

        // dirty hack, just reload it all!
        onResume()
    }

    // used both in initial startup, and when we return from editing
    // this clears both our LinearLayout holding Recipes, and our data set of these.
    override fun onResume() {
        super.onResume()

        // we'll want to remove all views, and currently stored recipes, so we can refresh them.
        recipes.clear()
        recipes_holder.removeAllViews()

        val intent = Intent(this, OpenRecipeActivity::class.java)

        val projection = arrayOf(
            Contract.RECIPE._ID,
            Contract.RECIPE.NAME,
            Contract.RECIPE.INSTRUCTIONS,
            Contract.RECIPE.RATING
        )

        val c = contentResolver.query(Contract.ALL_RECIPES, projection, null, null, sort)

        if(c != null) {
            c.moveToFirst()

            // Recipe.fromCursor() is static and allows for ez creation from our cursors
            recipes.add(Recipe.fromCursor(c))

            while (c.moveToNext()) {
                recipes.add(Recipe.fromCursor(c))
            }
        }

        for(r in recipes) {
            val add = RecipeView(this, r)
            add.setOnClickListener {
                intent.putExtra("recipe", r)
                startActivity(intent)
            }
            recipes_holder.addView(add)
        }

    }



}
