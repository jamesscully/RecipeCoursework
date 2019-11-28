package com.scullyapps.recipebook

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scullyapps.recipebook.data.Recipe
import com.scullyapps.recipebook.widgets.RecipeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var recipes = arrayListOf<Recipe>()
    val db = DBHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // close the initial db, databases should be created.
        db.close()

        val intent = Intent(this, OpenRecipe::class.java)


        btn_new_recipe.setOnClickListener {
            val name = "New Recipe"

            val cv = ContentValues()

            cv.put("name", name)
            cv.put("instructions", "")
            cv.put("rating", 1)

            val x = contentResolver.insert(Contract.ALL_RECIPES, cv)

            print("We've got ${x}")


        }


        val projection = arrayOf(
            Contract.RECIPE._ID,
            Contract.RECIPE.NAME,
            Contract.RECIPE.INSTRUCTIONS,
            Contract.RECIPE.RATING
        )

        val x = contentResolver.query(Uri.parse(Contract.ALL_RECIPES.toString() + "/2"), projection, null, null, null)

        x?.moveToFirst()


        print(x?.getString(0))

    }

    // used both in initial startup, and when we return from editing
    override fun onResume() {
        super.onResume()



        // we'll want to remove all views, and currently stored recipes, so we can refresh them.
        recipes.clear()
        recipes_holder.removeAllViews()

        val intent = Intent(this, OpenRecipe::class.java)


        val projection = arrayOf(
            Contract.RECIPE._ID,
            Contract.RECIPE.NAME,
            Contract.RECIPE.INSTRUCTIONS,
            Contract.RECIPE.RATING
        )

        val c = contentResolver.query(Contract.ALL_RECIPES, projection, null, null, null)

        if(c != null) {
            c.moveToFirst()
            recipes.add(recipeFromCursor(c))
            while (c.moveToNext()) {
                recipes.add(recipeFromCursor(c))
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


    fun recipeFromCursor(c : Cursor) : Recipe {
        return Recipe(
            c.getInt(0),
            c.getString(1),
            c.getString(2),
            c.getInt(3)
        )
    }

}
