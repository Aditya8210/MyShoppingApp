package com.wp7367.myshoppingapp.ui_layer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.useCase.GetAllOrderUseCase
import com.wp7367.myshoppingapp.domain_layer.useCase.OrderDetailUseCase
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val OrderSave: OrderDetailUseCase,
                                         private val GetAllOrder: GetAllOrderUseCase,
                                         private val repo: repo
                                         ): ViewModel() {

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    private val _verificationState = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val verificationState = _verificationState.asStateFlow()


    private val _getAllOrderDataState = MutableStateFlow(GetAllOrderDataState())
    val getAllOrderDataState = _getAllOrderDataState.asStateFlow()

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()



    fun orderDataSave(orderList: List<orderModel>){
        OrderSave.orderDetailUseCase(orderList).onEach {
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
        }.launchIn(viewModelScope)
    }

    fun verifyAndSaveOrder(paymentId: String, orderId: String, signature: String, orderList: List<orderModel>) {
        repo.verifyPaymentOnBackend(paymentId, orderId, signature).onEach { result ->
            when (result) {
                is ResultState.Loading -> {
                    _orderState.value = OrderState(isLoading = true)
                }
                is ResultState.Success -> {
                    // Create orders with 'Paid' status and valid transaction ID
                    val verifiedOrders = orderList.map { 
                        it.copy(transactionId = paymentId, status = "Paid") 
                    }
                    orderDataSave(verifiedOrders)
                }
                is ResultState.Error -> {
                    _orderState.value = OrderState(error = "Security Verification Failed: ${result.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }


    fun getAllOrderState(){
        GetAllOrder.getAllOrderUseCase().onEach {
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
        }.launchIn(viewModelScope)
    }

    fun setPaymentState(paymentId : String = "", signature: String = "", razorpayOrderId: String = "", errorMsg: String = "") {
        if(errorMsg.isEmpty()) {
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentId = paymentId,
                signature = signature,
                razorpayOrderId = razorpayOrderId,
                error = ""
            )
        }else{
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentId = "",
                signature = "",
                razorpayOrderId = "",
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
    val paymentId : String = "",
    val signature: String = "",
    val razorpayOrderId: String = "",
    val error : String = ""
)