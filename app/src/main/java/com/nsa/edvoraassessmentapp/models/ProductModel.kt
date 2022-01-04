package com.nsa.edvoraassessmentapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(
    val product_name:String,
    val brand_name:String,
    val price:Int,
    val address:Address,
    val discription:String,
    val date:String,
    val time:String,
    val image:String
): Parcelable {

    @Parcelize
    data class Address(
        val state:String,
        val city:String,
    ):Parcelable{}

}