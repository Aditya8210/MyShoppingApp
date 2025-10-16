package com.wp7367.myshoppingapp.ui_layer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.useCase.GetShippingAddressByIdUseCase
import com.wp7367.myshoppingapp.domain_layer.useCase.ShippingAddressUseCase
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.domain_layer.useCase.DeleteAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShippingViewModel @Inject constructor(private val ShippingAddress : ShippingAddressUseCase,
                                            private val GetShippingAddressById : GetShippingAddressByIdUseCase,
                                            private val DeleteAddress : DeleteAddressUseCase,

    ) : ViewModel() {


    private val _shippingAddressSt = MutableStateFlow(ShippingState())
    val shippingAddressSt = _shippingAddressSt.asStateFlow()

    private val _getShippingAdSt = MutableStateFlow(GetShippingAddressState())
    val getShippingAd = _getShippingAdSt.asStateFlow()

    private val _deleteAddressSt = MutableStateFlow(DeleteAddressState())
    val deleteAddressSt = _deleteAddressSt.asStateFlow()


    fun shippingAddress(shippingModel: shippingModel) {

        viewModelScope.launch {
            ShippingAddress.shippingAddressUseCase(shippingModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _shippingAddressSt.value = ShippingState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _shippingAddressSt.value = ShippingState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _shippingAddressSt.value = ShippingState(error = it.exception)
                    }
                }
            }
        }
    }

    //  ~ for reset the State After Submit ~

    fun resetState() {
        _shippingAddressSt.value = ShippingState()
    }


    fun getShippingDataById() {
        viewModelScope.launch {
            GetShippingAddressById.getShippingAddressByIdUseCase().collectLatest {

                when (it) {
                    is ResultState.Loading -> {
                        _getShippingAdSt.value = GetShippingAddressState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getShippingAdSt.value = GetShippingAddressState(data = it.data)

                    }

                    is ResultState.Error -> {
                        _getShippingAdSt.value = GetShippingAddressState(error = it.exception)
                    }
                }
            }
        }
    }

    fun deleteAddress(shippingModel: shippingModel) {

        viewModelScope.launch {
            DeleteAddress.deleteAddressUseCase(shippingModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _deleteAddressSt.value = DeleteAddressState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _deleteAddressSt.value = DeleteAddressState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _deleteAddressSt.value = DeleteAddressState(error = it.exception)
                    }
                }
            }

        }


    }
}


data class ShippingState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null
)

data class GetShippingAddressState(

    val isLoading: Boolean = false,
    val error: String = "",

    // Here Changing
    val data: List<shippingModel> = emptyList()
)


data class DeleteAddressState(

    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null,
)