package com.nsa.edvoraassessmentapp.repository

import com.nsa.edvoraassessmentapp.api.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepo @Inject constructor( private val apiInterface: ApiInterface){


    suspend fun getApiResult()=apiInterface.getProducts()

}