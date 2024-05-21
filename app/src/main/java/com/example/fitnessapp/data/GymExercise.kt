package com.example.fitnessapp.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GymExercise(
    @SerializedName("bodyPart")
    val bodyPart: String,
    @SerializedName("equipment")
    val equipment: String,
    @SerializedName("gifUrl")
    val gifUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instructions")
    val instructions: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>,
    @SerializedName("target")
    val target: String,
    @SerializedName("setCount")
    var setCount: Int = 1,



): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: ""
    )




    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bodyPart)
        parcel.writeString(equipment)
        parcel.writeString(gifUrl)
        parcel.writeString(id)
        parcel.writeStringList(instructions)
        parcel.writeString(name)
        parcel.writeStringList(secondaryMuscles)
        parcel.writeString(target)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GymExercise> {
        override fun createFromParcel(parcel: Parcel): GymExercise {
            return GymExercise(parcel)
        }

        override fun newArray(size: Int): Array<GymExercise?> {
            return arrayOfNulls(size)
        }
    }


}