package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repo: repo) {
    suspend fun getProductByIdUseCase(productId: String) = repo.getProductById(productId)

}