package com.wp7367.myshoppingapp.data_layer.repoimp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wp7367.myshoppingapp.common.CATEGORY
import com.wp7367.myshoppingapp.common.PRODUCTS
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.productsModel
import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImp @Inject constructor(private val FirebaseFirestore: FirebaseFirestore,
                                   private val FirebaseAuth: FirebaseAuth): repo{



    //  ~ Get All Category ~
    override suspend fun getAllCategory(): Flow<ResultState<List<category>>> = callbackFlow{


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

    //     ~ Get All Product ~
    override suspend fun getAllProduct(): Flow<ResultState<List<productsModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        FirebaseFirestore.collection(PRODUCTS).get().addOnSuccessListener {
            val productData = it.documents.mapNotNull {
                it.toObject(productsModel::class.java)

            }
            trySend(ResultState.Success(productData))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()

        }
    }


    //    ~ Register User With Email And Password ~
    override suspend fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)

        FirebaseAuth.createUserWithEmailAndPassword(userData.email,userData.password).addOnSuccessListener {
            FirebaseFirestore.collection("USERS").document(it.user?.uid.toString()).set(userData).addOnSuccessListener {

                trySend(ResultState.Success("User Register Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))

            }
        }
        awaitClose {
            close()
        }

    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            trySend(ResultState.Success("User Login Successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }

    }


}