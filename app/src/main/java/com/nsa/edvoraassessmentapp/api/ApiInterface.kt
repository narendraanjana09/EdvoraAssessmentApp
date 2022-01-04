package com.nsa.edvoraassessmentapp.api


import com.nsa.edvoraassessmentapp.models.ProductModel
import retrofit2.http.GET


interface ApiInterface {

    @GET(".")
    suspend fun getProducts():List<ProductModel>

}