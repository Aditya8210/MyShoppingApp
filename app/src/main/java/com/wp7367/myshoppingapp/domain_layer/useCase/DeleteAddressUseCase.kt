package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(private val repo: repo) {

    suspend fun deleteAddressUseCase(shippingModel: shippingModel) = repo.deleteShippingAddress(shippingModel)

}