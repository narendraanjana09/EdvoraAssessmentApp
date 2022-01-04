package com.nsa.edvoraassessmentapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsa.edvoraassessmentapp.models.ProductModel
import com.nsa.edvoraassessmentapp.repository.MainRepo
import kotlinx.coroutines.launch

class MainViewModel@ViewModelInject constructor(
    private val repo: MainRepo): ViewModel() {

    val productLiveData = MutableLiveData<List<ProductModel>>()


    fun getProducts(){
        viewModelScope.launch(){
            productLiveData.value=repo.getApiResult()
        }
    }

}