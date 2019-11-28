package com.scullyapps.recipebook.data

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable

data class Recipe(val id : Int, var name : String = "Default", var description : String = "Default", var rating : Int) : Parcelable {

    private val TAG: String = "Recipe";


    constructor(parcel: Parcel) : this(
        parcel.readInt(),

        // we should be fine with NPEs, as these values (even if empty)
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    var ingredients : ArrayList<Ingredient> = arrayListOf()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(rating)
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }

}