package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class GetAllProductUseCase @Inject constructor(private val repo: repo)  {

    fun getAllProductUseCase() = repo.getAllProduct()
}