package com.wp7367.myshoppingapp.ui_layer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.useCase.GetAllOrderUseCase
import com.wp7367.myshoppingapp.domain_layer.useCase.OrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val OrderSave: OrderDetailUseCase,
                                         private val GetAllOrder: GetAllOrderUseCase,

                                         ): ViewModel() {

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()


    private val _getAllOrderDataState = MutableStateFlow(GetAllOrderDataState())
    val getAllOrderDataState = _getAllOrderDataState.asStateFlow()

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()



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


    fun getAllOrderState(){
        viewModelScope.launch {
            GetAllOrder.getAllOrderUseCase().collectLatest{
                when(it) {
                    is ResultState.Loading -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(error = it.exception)
                    }
                }
            }
        }
    }

    fun setPaymentState(paymentId : String = "",errorMsg: String = "") {
        if(errorMsg.isEmpty()) {
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentState = paymentId,
                error = ""
            )
        }else{
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentState = "",
                error = errorMsg
            )
        }
    }

    fun clearOrderState(){
        _orderState.value = OrderState()
    }
    fun clearPaymentState(){
        _paymentState.value = PaymentState()
    }


}

data class OrderState(
    val isLoading : Boolean = false,
    val data : String = "",
    val error : String = ""
)

data class GetAllOrderDataState(
    val isLoading : Boolean = false,
    val data : List<orderModel> = emptyList(),
    val error : String = ""
)

data class PaymentState(
    val isLoading : Boolean = false,
    val paymentState : String = "",
    val error : String = ""
)