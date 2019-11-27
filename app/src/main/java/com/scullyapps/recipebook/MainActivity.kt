package com.scullyapps.recipebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, OpenRecipe::class.java)

        // generate databases
        val db = DBHelper(this)
        val wdb = db.writableDatabase
        wdb.close()
        db.close()

        startActivity(intent)
    }
}
