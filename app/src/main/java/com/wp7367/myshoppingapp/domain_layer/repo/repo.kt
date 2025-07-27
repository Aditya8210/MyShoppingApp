package com.wp7367.myshoppingapp.domain_layer.repo

import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.category
import kotlinx.coroutines.flow.Flow

interface repo  {

    fun getAllCategory(): Flow <ResultState<List<category>>>
}