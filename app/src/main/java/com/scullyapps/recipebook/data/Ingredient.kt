package com.scullyapps.recipebook.data

import android.os.Parcel
import android.os.Parcelable

class Ingredient(id : Int, name : String = "Default", amount : String = "Default") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    private val TAG: String = "Ingredient"

    val id = id
    var name = name
    var amount = amount

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