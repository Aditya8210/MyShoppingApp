package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class RegisterUserWithEmailPassUseCase @Inject constructor(private val repo: repo){

  suspend  fun registerUserWithEmailPassUseCase(userData: userData) = repo.registerUserWithEmailAndPassword(userData)


}