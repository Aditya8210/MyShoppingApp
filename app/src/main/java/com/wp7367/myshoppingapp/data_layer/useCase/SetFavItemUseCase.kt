package com.wp7367.myshoppingapp.data_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class SetFavItemUseCase@Inject constructor(private val repo: repo) {

    suspend fun setFavItemUseCase(favouriteModel: favouriteModel) = repo.setFavItem(favouriteModel)
}