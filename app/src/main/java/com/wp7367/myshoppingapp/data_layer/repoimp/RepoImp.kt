package com.wp7367.myshoppingapp.data_layer.repoimp

import com.google.firebase.firestore.FirebaseFirestore
import com.wp7367.myshoppingapp.common.CATEGORY
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImp @Inject constructor(private val FirebaseFirestore : FirebaseFirestore): repo{
    override fun getAllCategory(): Flow<ResultState<List<category>>> = callbackFlow{


        trySend(ResultState.Loading)

        FirebaseFirestore.collection(CATEGORY).get().addOnSuccessListener {

            val categoryData = it.documents.mapNotNull {
                it.toObject(category::class.java)
            }

            trySend(ResultState.Success(categoryData))


        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }

    }

}