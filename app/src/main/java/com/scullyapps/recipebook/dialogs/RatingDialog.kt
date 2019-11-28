package com.scullyapps.recipebook.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.RatingBar
import com.scullyapps.recipebook.R
import kotlinx.android.synthetic.main.dialog_rating.*

class RatingDialog(context: Context, recipeName : String, currentRate : Float) : Dialog(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert) {
    private val TAG: String = "RatingDialog"

    private val layoutID = R.layout.dialog_rating

    var ok : Button
    var r  : RatingBar

    init {
        setContentView(layoutID)
        setCancelable(true)
        window?.setBackgroundDrawableResource(R.drawable.bdr_soft)

        ok = findViewById(R.id.dialog_rating_ok)
        r  = findViewById(R.id.dialog_rating)

        // this concatenates the name onto the text as shown in dialog_rating.xml
        dialog_rating_name.text = (dialog_rating_name.text.toString() + recipeName)

        // sets our current rating for the recipe
        r.rating = currentRate


        r.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if(ratingBar.rating < 1.0f) {
                ratingBar.rating = 1.0f
            }
        }

    }





}