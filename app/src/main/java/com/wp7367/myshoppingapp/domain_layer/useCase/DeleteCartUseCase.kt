package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(private val repo: repo) {

    suspend fun deleteCartUseCase(cartItemModel: cartItemModel) = repo.deleteCartItem(cartItemModel)
}