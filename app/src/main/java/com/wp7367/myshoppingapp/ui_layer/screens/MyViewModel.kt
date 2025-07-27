package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.data_layer.useCase.GetAllCategoryUseCase
import com.wp7367.myshoppingapp.domain_layer.models.category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val GetAllCategory: GetAllCategoryUseCase) : ViewModel() {



  //   --Here State is Manage --

    private val _getAllCategorySt = MutableStateFlow(getCategoryState())
    val getAllCategory = _getAllCategorySt.asStateFlow()



   fun getAllCategory() {

       viewModelScope.launch {
           GetAllCategory.getAllCategoryUseCase().collectLatest{
               when(it){
                   is ResultState.Loading -> {
                       _getAllCategorySt.value = getCategoryState(isLoading = true)
                   }
                   is ResultState.Success -> {
                       _getAllCategorySt.value = getCategoryState(data = it.data)
                   }
                   is ResultState.Error -> {
                       _getAllCategorySt.value = getCategoryState(error = it.exception)
                   }
               }

           }

       }

   }




}

data class getCategoryState(

    val isLoading: Boolean = false,
    val error: String = "",

    // Here Changing
    val data: List<category?> = emptyList()
)


