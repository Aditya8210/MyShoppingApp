package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class SetCartItemUseCase @Inject constructor(private val repo: repo) {

    suspend fun setCartItemUseCase(cartItemModel: cartItemModel) = repo.setCartItem(cartItemModel)
}