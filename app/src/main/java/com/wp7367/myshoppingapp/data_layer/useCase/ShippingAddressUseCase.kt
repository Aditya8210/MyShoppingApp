package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class ShippingAddressUseCase @Inject constructor(private val repo: repo) {

    suspend fun shippingAddressUseCase(shippingModel: shippingModel) = repo.shippingAddress(shippingModel)
}