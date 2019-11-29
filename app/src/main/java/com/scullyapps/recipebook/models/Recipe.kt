package com.scullyapps.recipebook.models

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.scullyapps.recipebook.data.Contract

data class Recipe(val id : Int, var name : String = "Default", var description : String = "Default", var rating : Int) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),

        // we should be fine with NPEs, as these values (even if empty)
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

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

        fun fromCursor(c : Cursor) : Recipe{
            return Recipe(
                c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getInt(3)
            )
        }

        fun fromUri(uri : Uri?, cr: ContentResolver) : Recipe {

            if(uri == null)
                throw Exception("Unable to use Uri")

            val projection = arrayOf(
                Contract.RECIPE._ID,
                Contract.RECIPE.NAME,
                Contract.RECIPE.INSTRUCTIONS,
                Contract.RECIPE.RATING
            )

            val c = cr.query(uri, projection, null, null, null)

            if(c == null)
                throw Exception("Unable to find recipe")
            else {
                c.moveToFirst()
                return fromCursor(c)
            }


        }
    }

}