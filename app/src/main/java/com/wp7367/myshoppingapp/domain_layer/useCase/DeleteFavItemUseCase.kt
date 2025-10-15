package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class DeleteFavItemUseCase @Inject constructor(private val repo: repo) {

    suspend fun deleteFavItemUseCase(favouriteModel: favouriteModel) = repo.deleteFavItem(favouriteModel)
}