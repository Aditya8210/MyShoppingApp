package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(private val repo: repo)  {

 suspend   fun getAllCategoryUseCase() = repo.getAllCategory()
}