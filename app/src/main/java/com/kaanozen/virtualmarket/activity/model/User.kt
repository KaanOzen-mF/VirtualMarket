package com.kaanozen.virtualmarket.activity.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Create a data model class for User with the required fields.



//A data model class for User with required fields.

@Parcelize
data class User (val id: String = "",
                 val firstName: String = "",
                 val lastName: String = "",
                 val email: String = "",
                 val mobile: Long = 0) : Parcelable

