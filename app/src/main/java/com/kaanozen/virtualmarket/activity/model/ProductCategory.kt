package com.kaanozen.virtualmarket.activity.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ProductCategory(
    var id : String = "",
    var name : String = "",
    var parentID : String = "",
    var isLeaf : Boolean = true,
): Parcelable, MarketItem