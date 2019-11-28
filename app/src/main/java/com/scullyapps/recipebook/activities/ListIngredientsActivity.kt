package com.scullyapps.recipebook.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.recipebook.R
import com.scullyapps.recipebook.data.Contract
import com.scullyapps.recipebook.data.DBHelper
import com.scullyapps.recipebook.widgets.IngredientView
import kotlinx.android.synthetic.main.activity_list_ingredients.*

class ListIngredientsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ingredients)


        val projection = arrayOf(
            Contract.INGREDIENTS._ID,
            Contract.INGREDIENTS.NAME
        )

        val c = contentResolver.query(Contract.ALL_INGREDIENTS, projection, null, null, null)

        if(c == null) throw Exception("Cursor for ingredients is null")

        c.moveToFirst()


        // using for the doesIngredAssosciate function I wrote
        // i.e. if it associates, it cant be removed, else it can.
        val db = DBHelper(this)

        while(!c.isAfterLast) {

            val name = c.getString(1)
            val id   = c.getInt(0)

            val add = IngredientView(this,  name, "", id)

            add.isRemoveable(!db.doesIngredAssociate(id))

            add.btnRemove.setOnClickListener {
                contentResolver.delete(Contract.ALL_INGREDIENTS, add.ingId.toString(), null)
                add.visibility = View.GONE
            }

            list_ing.addView(add)
            c.moveToNext()
        }

    }
}
