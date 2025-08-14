package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class UpdateUserByIdUseCase@Inject constructor(private val repo: repo) {
    suspend fun  updateUserByIdUseCase(userData: userData) = repo.updateUserData(userData)

}