package com.kaanozen.virtualmarket.activity.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Order (
        var id : String = "",
        var userID : String = "",
        var productID : String = "",
        var quantitiy : Int = 0,
        var cost : Double = 0.0,
        var productName : String = ""
) : Parcelable {
}