package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject


class LoginUserWithEmailPassUseCase @Inject constructor(private val repo: repo){
    suspend fun loginUserWithEmailPassUseCase(email: String, password: String,
                                              ) = repo.loginWithEmailAndPassword(email,password)
}
