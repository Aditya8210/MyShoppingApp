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

  fun getAllCategory(): Flow <ResultState<List<category>>>




  fun getAllProduct():Flow<ResultState<List<ProductModel>>>

  fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>>

  fun loginWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>>


  fun getUserById(uid: String): Flow<ResultState<userData>>

  fun updateUserData(userData: userData): Flow<ResultState<String>>

  fun getProductById(productId: String): Flow<ResultState<ProductModel>>

  fun getBanner(): Flow<ResultState<List<String>>>
  
  fun setCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>

  fun getCartItem(): Flow<ResultState<List<cartItemModel>>>

  fun deleteCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>

  fun setFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>

  fun getFavItem(): Flow<ResultState<List<favouriteModel>>>

  fun deleteFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>

  fun searchFeature(query: String): Flow<ResultState<List<ProductModel>>>

  fun shippingAddress(shippingModel: shippingModel): Flow<ResultState<String>>

  fun deleteShippingAddress(shippingModel: shippingModel): Flow<ResultState<String>>

  fun showShippingAddressById(): Flow<ResultState<List<shippingModel>>>

  fun orderDataSave(orderList: List<orderModel>) : Flow<ResultState<String>>

  fun getOrderData(): Flow<ResultState<List<orderModel>>>

  fun verifyPaymentOnBackend(
      paymentId: String,
      orderId: String,
      signature: String
  ): Flow<ResultState<String>>





}