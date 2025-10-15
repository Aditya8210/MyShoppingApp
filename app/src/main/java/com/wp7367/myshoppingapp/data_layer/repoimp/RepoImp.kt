package com.wp7367.myshoppingapp.data_layer.repoimp


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.wp7367.myshoppingapp.common.CART
import com.wp7367.myshoppingapp.common.CATEGORY
import com.wp7367.myshoppingapp.common.FAVOURITE
import com.wp7367.myshoppingapp.common.ORDER
import com.wp7367.myshoppingapp.common.ORDER_DATA
import com.wp7367.myshoppingapp.common.PRODUCT

import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.common.SHIPPING_DATA
import com.wp7367.myshoppingapp.common.USERS

import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel

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

            trySend(ResultState.Success( categoryData))


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
        if (uid.isEmpty()) {
            trySend(ResultState.Error("User ID cannot be empty."))
        } else {
            trySend(ResultState.Loading)
            FirebaseFirestore.collection(USERS)
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val user = doc.toObject(userData::class.java)?.apply { this.uid = doc.id }
                    if (user != null) trySend(ResultState.Success(user))
                    else trySend(ResultState.Error("User not found or could not be parsed."))
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Error(e.message ?: "Failed to fetch user data."))
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


    // ~ Get Product by Id ~
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

    // ~ Get Banner ~
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


    // ~ SetCartItem ~
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
            .collection(CART)
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

    // ~ GetCartItem ~
    override suspend fun getCartItem(): Flow<ResultState<List<cartItemModel>>>  = callbackFlow {

        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        val cartRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection(CART)
        cartRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ResultState.Error(error.message ?: "Failed to fetch cart due to an error."))
                return@addSnapshotListener
            }
            val cartItems = snapshot?.documents?.mapNotNull { document ->
                document.toObject(cartItemModel::class.java)
            } ?: emptyList()
            trySend(ResultState.Success(cartItems))
        }
        awaitClose {
            close()
        }
    }


    // ~ DeleteCartItem ~
    override suspend fun deleteCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>> = callbackFlow{

        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        val cartRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection(CART)
            .document(cartItemModel.productId)
            .delete()
            .addOnSuccessListener {
                trySend(ResultState.Success("Item Deleted Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }

    // ~ SetFavItem ~
    override suspend fun setFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>  = callbackFlow{

        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val favRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection(FAVOURITE)
            .document(favouriteModel.productId)

        favRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Item exists, inform the user
                trySend(ResultState.Success("Item already in favourites"))
            } else {
                // Item does not exist, so add it
                favRef.set(favouriteModel)
                    .addOnSuccessListener {
                        trySend(ResultState.Success("Item added to favourites"))
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Error(e.message ?: "Failed to add item to favourites."))
                    }
            }
        }.addOnFailureListener { e ->
            trySend(ResultState.Error(e.message ?: "Failed to check favourite status."))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getFavItem(): Flow<ResultState<List<favouriteModel>>>  = callbackFlow{

        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        val favRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection(FAVOURITE)
        favRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ResultState.Error(error.message ?: "Failed to fetch favourites due to an error."))
                return@addSnapshotListener
            }
            val favItems = snapshot?.documents?.mapNotNull { document ->
                document.toObject(favouriteModel::class.java)
            } ?: emptyList()
            trySend(ResultState.Success(favItems))
        }
        awaitClose {
            close()
        }

    }

    override suspend fun deleteFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>  = callbackFlow{

        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val favRef = FirebaseFirestore.collection(USERS)
            .document(currentUser.uid)
            .collection(FAVOURITE)
            .document(favouriteModel.productId)
            .delete()
            .addOnSuccessListener {
                trySend(ResultState.Success("Item Deleted Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }

    override suspend fun searchFeature(query: String): Flow<ResultState<List<ProductModel>>>  =callbackFlow{

        trySend(ResultState.Loading)
        FirebaseFirestore.collection(PRODUCT).orderBy("name")

            .startAt(query).get()
            .addOnSuccessListener {
                val products = it.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)?.apply {
                        productId =it.id
                    }
                }
                trySend(ResultState.Success(products))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun shippingAddress(shippingModel: shippingModel): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        FirebaseFirestore.collection(SHIPPING_DATA)
            .document(FirebaseAuth.currentUser?.uid.toString()).set(shippingModel)
            .addOnSuccessListener {
                trySend(ResultState.Success("Address Saved Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun showShippingAddressById(): Flow<ResultState<List<shippingModel>>> = callbackFlow {

        trySend(ResultState.Loading)

        FirebaseFirestore.collection(SHIPPING_DATA)
            .document(FirebaseAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                val shippingData = it.toObject(shippingModel::class.java)

                trySend(ResultState.Success(listOfNotNull(shippingData)))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }


    }

    override suspend fun orderDataSave(orderList: List<orderModel>): Flow<ResultState<String>>  = callbackFlow{

        trySend(ResultState.Loading)
        val orderMap =
            orderList.mapIndexed { index, order -> index.toString() to order }.toMap()
        FirebaseFirestore.collection(ORDER_DATA)
            .document(FirebaseAuth.currentUser?.uid.toString()).collection(ORDER).document()
            .set(orderMap).addOnSuccessListener {
                trySend(ResultState.Success("Order Successfully"))
//                pushNotification.sendNotification(
//                    title = "Order Initiate",
//                    message = "Order Successfully"
//                )
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }


}
