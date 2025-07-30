package com.wp7367.myshoppingapp.domain_layer.repo

import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.productsModel
import com.wp7367.myshoppingapp.domain_layer.models.userData
import kotlinx.coroutines.flow.Flow

interface repo  {

  suspend  fun getAllCategory(): Flow <ResultState<List<category>>>






  suspend fun getAllProduct():Flow<ResultState<List<productsModel>>>

  suspend fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>>

  suspend fun loginWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>>
}