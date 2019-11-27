package com.scullyapps.recipebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scullyapps.recipebook.data.Recipe
import com.scullyapps.recipebook.widgets.RecipeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var recipes = arrayListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, OpenRecipe::class.java)

        // generate databases
        val db = DBHelper(this)
        val wdb = db.writableDatabase
        wdb.close()
        db.close()

        val projection = arrayOf(
            Contract.RECIPE._ID,
            Contract.RECIPE.NAME,
            Contract.RECIPE.INSTRUCTIONS,
            Contract.RECIPE.RATING
        )

        val c = contentResolver.query(Contract.URECIPE, projection, null, null, null)

        if(c != null) {
            c.moveToFirst()
            recipes.add(Recipe(c))

            while (c.moveToNext()) {
                recipes.add(Recipe(c))
            }
        }

        for(r in recipes) {

            val add = RecipeView(this, r)

            add.setOnClickListener {
                intent.putExtra("recipe", r)
            }

            recipes_holder.addView(add)
        }


        //startActivity(intent)
    }
}
