package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class GetProductBySearchUseCase @Inject constructor(private val repo: repo) {

    suspend fun getProductBySearchUseCase(query: String) = repo.searchFeature(query)
}