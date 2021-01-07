package com.kaanozen.virtualmarket.activity.model

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product (
        var id : String = "",
        var name : String = "",
        var information : String = "",
        var price : Double = 0.0,
        var stock : Int = 0,
        var parentID : String = "",
): Parcelable, MarketItem