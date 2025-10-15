package com.wp7367.myshoppingapp.domain_layer.repo

import com.wp7367.myshoppingapp.common.ResultState

import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel

import com.wp7367.myshoppingapp.domain_layer.models.userData
import kotlinx.coroutines.flow.Flow

interface repo  {

  suspend  fun getAllCategory(): Flow <ResultState<List<category>>>




  suspend fun getAllProduct():Flow<ResultState<List<ProductModel>>>

  suspend fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>>

  suspend fun loginWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>>


  suspend fun getUserById(uid: String): Flow<ResultState<userData>>

  suspend fun updateUserData(userData: userData): Flow<ResultState<String>>

  suspend fun getProductById(productId: String): Flow<ResultState<ProductModel>>

  suspend fun getBanner(): Flow<ResultState<List<String>>>
  
  suspend fun setCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>

  suspend fun getCartItem(): Flow<ResultState<List<cartItemModel>>>

  suspend fun deleteCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>

  suspend fun setFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>

  suspend fun getFavItem(): Flow<ResultState<List<favouriteModel>>>

  suspend fun deleteFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>

  suspend fun searchFeature(query: String): Flow<ResultState<List<ProductModel>>>

  suspend fun shippingAddress(shippingModel: shippingModel): Flow<ResultState<String>>

  suspend fun showShippingAddressById(): Flow<ResultState<List<shippingModel>>>

  suspend fun orderDataSave(orderList: List<orderModel>) : Flow<ResultState<String>>



}