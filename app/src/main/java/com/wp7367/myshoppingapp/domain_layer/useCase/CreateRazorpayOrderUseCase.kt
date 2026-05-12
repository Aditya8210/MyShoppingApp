package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class CreateRazorpayOrderUseCase @Inject constructor(private val repo: repo) {
    fun createRazorpayOrder(amount: Double, userId: String) = repo.createRazorpayOrder(amount, userId)
}
