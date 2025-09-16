package com.wp7367.myshoppingapp.data_layer.repoimp

import android.util.Log // Added this import
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.wp7367.myshoppingapp.common.CATEGORY
import com.wp7367.myshoppingapp.common.PRODUCT

import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.common.USERS

import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel

import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.domain_layer.repo.repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.jvm.java

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
    override suspend fun getAllProduct(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        FirebaseFirestore.collection(PRODUCT).get().addOnSuccessListener { querySnapshot -> // querySnapshot contains multiple documents
            val productData = querySnapshot.documents.mapNotNull { documentSnapshot ->
                val product = documentSnapshot.toObject(ProductModel::class.java)
                product?.let {
                    // Assign the document ID to the productId field
                    it.productId = documentSnapshot.id
                }
                product // return the product (or null if conversion failed)
            }
            trySend(ResultState.Success(productData))
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception.message.toString()))
        }
        awaitClose {
            close()

        }
    }


    //    ~ Register User With Email And Password ~
    override suspend fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)

        FirebaseAuth.createUserWithEmailAndPassword(userData.email,userData.password).addOnSuccessListener {
            FirebaseFirestore.collection(USERS).document(it.user?.uid.toString()).set(userData).addOnSuccessListener {

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


    //     ~ Get User By Id ~
    override suspend fun getUserById(uid: String): Flow<ResultState<userData>> = callbackFlow {
        trySend(ResultState.Loading)
        if (uid.isEmpty()) {
            trySend(ResultState.Error("User ID cannot be empty."))
        } else {
            FirebaseFirestore.collection(USERS)
                .document(uid).get().addOnSuccessListener { documentSnapshot -> // Renamed 'it' for clarity
                    documentSnapshot.toObject(userData::class.java)?.let { user ->
                        user.uid = documentSnapshot.id // Set uid on the non-null user
                        trySend(ResultState.Success(user))
                    } ?: run {
                        // This block executes if toObject() returns null
                        trySend(ResultState.Error("User data for UID '$uid' could not be found or parsed."))
                    }
                }
                .addOnFailureListener { exception -> // Network issue Our other Issue Occur
                    trySend(ResultState.Error(exception.message ?: "Failed to fetch user data for UID '$uid' due to an error."))
                }
        }
        awaitClose {
            close()
        }

    }



    override suspend fun updateUserData(userData: userData): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        FirebaseFirestore.collection(USERS).document(FirebaseAuth.uid.toString())
            .set(userData).addOnSuccessListener {
                trySend(ResultState.Success("User Data Updated Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                }
        awaitClose {
            close()

            }
    }


    // ~Get Product by Id~
    override suspend fun getProductById(productId: String): Flow<ResultState<ProductModel>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseFirestore.collection(PRODUCT).document(productId).get()
            .addOnSuccessListener {  document ->

                val product = document.toObject(ProductModel::class.java)
                product?.productId = document.id
                trySend(ResultState.Success(product!!))

            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    // ~Get Banner ~
    override suspend fun getBanner(): Flow<ResultState<List<String>>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseFirestore.collection("bannerdata").document("Banner").get()
            .addOnSuccessListener { document ->
                val bannerList = document.get("urls") as List<String>
                trySend(ResultState.Success(bannerList))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }

    override suspend fun setCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>  = callbackFlow{
        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close() 
            return@callbackFlow
        }

        val cartRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection("cart")
            .document(cartItemModel.productId) // Use productId as document ID

        FirebaseFirestore.runTransaction { transaction ->
            val snapshot = transaction.get(cartRef)
            if (snapshot.exists()) {
                val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 0

                transaction.update(cartRef, "quantity", currentQuantity + cartItemModel.quantity)

                "Quantity updated for product: ${cartItemModel.productId}" // Message for success
            } else {
                transaction.set(cartRef, cartItemModel)
                "Product added to cart: ${cartItemModel.productId}" // Message for success
            }
        }.addOnSuccessListener { successMessage ->
            trySend(ResultState.Success(successMessage))

        }.addOnFailureListener { e ->
            trySend(ResultState.Error(e.message ?: "Failed to update cart due to an unknown error."))
        }

        awaitClose {
            close()
        }
    }
}
