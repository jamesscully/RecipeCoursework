package com.scullyapps.recipebook.models

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable

// this is a data class - we only need to define the ID as we have default parameters in place.
// in Kotlin, a data-class defines typical functions such as hashCode, equals, and makes parcels a breeze.
data class Ingredient(val id : Int, var name : String = "Default", var amount : String = "Default") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel : Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }

    }
}