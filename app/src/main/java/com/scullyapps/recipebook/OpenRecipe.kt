package com.scullyapps.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// this class is used to rate, delete or edit an activity.

class OpenRecipe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_recipe)
    }
}
