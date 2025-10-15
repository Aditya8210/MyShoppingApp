package com.wp7367.myshoppingapp.ui_layer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.useCase.OrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val OrderSave: OrderDetailUseCase): ViewModel() {

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()



    fun orderDataSave(orderList: List<orderModel>){
        viewModelScope.launch {
            OrderSave.orderDetailUseCase(orderList).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _orderState.value = OrderState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _orderState.value = OrderState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _orderState.value = OrderState(error = it.exception)
                    }
                }
            }
        }
    }


}

data class OrderState(
    val isLoading : Boolean = false,
    val data : String = "",
    val error : String = ""
)