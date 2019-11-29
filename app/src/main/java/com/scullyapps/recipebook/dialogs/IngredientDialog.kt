package com.scullyapps.recipebook.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.scullyapps.recipebook.R

class IngredientDialog(context: Context) : Dialog(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert){

    private val layoutID = R.layout.dialog_ingredient

    private var txtName : EditText
    private var txtAmt  : EditText

    var ok : Button

    init {

        setContentView(layoutID)
        setCancelable(true)
        window?.setBackgroundDrawableResource(R.drawable.bdr_soft)

        txtName = findViewById(R.id.dialog_ingred_name)
        txtAmt  = findViewById(R.id.dialog_ingred_amount)

        ok = findViewById(R.id.dialog_ingred_ok)
    }

    fun getName() : String {
        return txtName.text.toString()
    }

}