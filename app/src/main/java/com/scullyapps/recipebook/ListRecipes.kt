package com.scullyapps.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// this should hold a listview of recipes,
// and open an activity that allows adding one, deleting, or rate

class ListRecipes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipes)
    }
}
