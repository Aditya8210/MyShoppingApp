package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class OrderDetailUseCase @Inject constructor(private val repo: repo) {

    suspend fun orderDetailUseCase(orderList: List<orderModel>)=repo.orderDataSave(orderList)
}