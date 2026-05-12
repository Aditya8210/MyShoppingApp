package com.wp7367.myshoppingapp.data_layer.repoimp


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

import com.wp7367.myshoppingapp.common.CART
import com.wp7367.myshoppingapp.common.CATEGORY
import com.wp7367.myshoppingapp.common.FAVOURITE
import com.wp7367.myshoppingapp.common.ORDER
import com.wp7367.myshoppingapp.common.ORDER_DATA
import com.wp7367.myshoppingapp.common.PRODUCT

import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.common.SHIPPING_DATA
import com.wp7367.myshoppingapp.common.USERS

import com.wp7367.myshoppingapp.data_layer.remote.PaymentApiService
import com.wp7367.myshoppingapp.data_layer.remote.PaymentVerificationRequest
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
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.java

class RepoImp @Inject constructor(private val FirebaseFirestore: FirebaseFirestore,
                                   private val FirebaseAuth: FirebaseAuth,
                                   private val paymentApiService: PaymentApiService): repo{



    //  ~ Get All Category ~
    override fun getAllCategory(): Flow<ResultState<List<category>>> = callbackFlow{


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
    override fun getAllProduct(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
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
    override fun registerUserWithEmailAndPassword(userData: userData): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)

        FirebaseAuth.createUserWithEmailAndPassword(userData.email,userData.password).addOnSuccessListener {
            FirebaseFirestore.collection(USERS).document(it.user?.uid.toString()).set(userData)
            .addOnSuccessListener {
                trySend(ResultState.Success("User Register Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))

            }

            updateFcmToken(FirebaseAuth.currentUser?.uid.toString())


        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }

    }

    //~ Login User With Email And Password ~
    override fun loginWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            trySend(ResultState.Success("User Login Successfully"))

            updateFcmToken(FirebaseAuth.currentUser?.uid.toString())

        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }

    }


    //     ~ Get User By Id ~
    override fun getUserById(uid: String): Flow<ResultState<userData>> = callbackFlow {
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


    //~ Update User Data ~
    override fun updateUserData(userData: userData): Flow<ResultState<String>> = callbackFlow {

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
    override fun getProductById(productId: String): Flow<ResultState<ProductModel>> = callbackFlow {
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
    override fun getBanner(): Flow<ResultState<List<String>>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseFirestore.collection("bannerdata").document("Banner").get()
            .addOnSuccessListener { document ->
                val bannerList = document.get("urls") as? List<String> ?: emptyList()
                trySend(ResultState.Success(bannerList))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }


    // ~ SetCartItem ~
    override fun setCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>>  = callbackFlow{
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

        cartRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 0
                val newQuantity = currentQuantity + cartItemModel.quantity

                if (cartItemModel.quantity > 0 && newQuantity > cartItemModel.availableUnits) {
                    trySend(ResultState.Error("Limit reached! Only ${cartItemModel.availableUnits} units available."))
                } else {
                    cartRef.update("quantity", com.google.firebase.firestore.FieldValue.increment(cartItemModel.quantity.toLong()))
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Quantity updated"))
                        }
                        .addOnFailureListener { e ->
                            trySend(ResultState.Error(e.message ?: "Failed to update quantity"))
                        }
                }
            } else {
                // If it doesn't exist, set the initial data
                if (cartItemModel.quantity > cartItemModel.availableUnits) {
                    trySend(ResultState.Error("Only ${cartItemModel.availableUnits} units available."))
                } else {
                    cartRef.set(cartItemModel)
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Added to cart"))
                        }
                        .addOnFailureListener { e ->
                            trySend(ResultState.Error(e.message ?: "Failed to add to cart"))
                        }
                }
            }
        }.addOnFailureListener { e ->
            trySend(ResultState.Error(e.message ?: "Failed to check cart item"))
        }

        awaitClose {
            close()
        }
    }

    // ~ GetCartItem ~
    override fun getCartItem(): Flow<ResultState<List<cartItemModel>>>  = callbackFlow {

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
    override fun deleteCartItem(cartItemModel: cartItemModel): Flow<ResultState<String>> = callbackFlow{

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
    override fun setFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>  = callbackFlow{

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

    // ~ GetFavItem ~
    override fun getFavItem(): Flow<ResultState<List<favouriteModel>>>  = callbackFlow{

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

    //~ DeleteFavItem ~
    override fun deleteFavItem(favouriteModel: favouriteModel): Flow<ResultState<String>>  = callbackFlow{

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

    //~ SearchFeature ~
    override fun searchFeature(query: String): Flow<ResultState<List<ProductModel>>>  =callbackFlow{

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

    //~ ShippingAddress ~
    override fun shippingAddress(shippingModel: shippingModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        
        val ref = FirebaseFirestore.collection(SHIPPING_DATA)
            .document(currentUser.uid)
            .collection("addresses")
            
        ref.get().addOnSuccessListener { snapshot ->
            val isFirst = snapshot.isEmpty
            val docRef = if (shippingModel.addressId.isEmpty()) ref.document() else ref.document(shippingModel.addressId)
            
            val finalModel = shippingModel.copy(
                addressId = docRef.id,
                selected = if (isFirst) true else shippingModel.selected
            )

            docRef.set(finalModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Address Saved Successfully"))
                    close()
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                    close()
                }
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
            close()
        }
        awaitClose { }
    }

    //~ DeleteShippingAddress ~
    override fun deleteShippingAddress(shippingModel: shippingModel): Flow<ResultState<String>>  = callbackFlow{

        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        FirebaseFirestore.collection(SHIPPING_DATA)
            .document(currentUser.uid)
            .collection("addresses")
            .document(shippingModel.addressId)
            .delete()
            .addOnSuccessListener {
                trySend(ResultState.Success("Address Deleted Successfully"))
                close()
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }
        awaitClose { }
    }

    //~ ShowShippingAddressById ~
    override fun showShippingAddressById(): Flow<ResultState<List<shippingModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val listener = FirebaseFirestore.collection(SHIPPING_DATA)
            .document(currentUser.uid)
            .collection("addresses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error(error.message.toString()))
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(shippingModel::class.java)?.apply { addressId = doc.id }
                    }
                    trySend(ResultState.Success(list))
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    override fun selectAddress(shippingModel: shippingModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val ref = FirebaseFirestore.collection(SHIPPING_DATA)
            .document(currentUser.uid)
            .collection("addresses")

        ref.get().addOnSuccessListener { snapshot ->
            val batch = FirebaseFirestore.batch()
            snapshot.documents.forEach { doc ->
                val isSelected = doc.id == shippingModel.addressId
                batch.update(doc.reference, "selected", isSelected)
            }
            batch.commit().addOnSuccessListener {
                trySend(ResultState.Success("Address Selected"))
                close()
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
            close()
        }
        awaitClose { }
    }

    //~ OrderDataSave ~
    override fun orderDataSave(orderList: List<orderModel>): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val user = FirebaseAuth.currentUser
        if (user == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        val newOrderRef = FirebaseFirestore.collection(ORDER_DATA)
            .document(user.uid)
            .collection(ORDER)
            .document()

        val orderId = newOrderRef.id
        val updatedOrderList = orderList.map { it.copy(orderId = orderId) }
        val orderMap = updatedOrderList.mapIndexed { index, order -> index.toString() to order }.toMap()

        newOrderRef.set(orderMap)
            .addOnSuccessListener {
                trySend(ResultState.Success("Order Successfully"))
                close()
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.message ?: "Failed to save order"))
                close()
            }

        awaitClose { }
    }

    //~ GetOrderData ~
    override fun getOrderData(): Flow<ResultState<List<orderModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val user = FirebaseAuth.currentUser
        if (user == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        FirebaseFirestore.collection(ORDER_DATA)
            .document(user.uid)
            .collection(ORDER)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val allOrders = mutableListOf<orderModel>()

                querySnapshot.documents.forEach { document ->
                    val maps = document.data
                    maps?.forEach { (_, value) ->
                        val orderMap = value as? Map<*, *>
                        orderMap?.let {
                            val order = orderModel(
                                orderId = it["orderId"] as? String ?: "",
                                productId = it["productId"] as? String ?: "",
                                productName = it["productName"] as? String ?: "",
                                productDescription = it["productDescription"] as? String ?: "",
                                productQty = it["productQty"] as? String ?: "",
                                productFinalPrice = it["productFinalPrice"] as? String ?: "",
                                productCategory = it["productCategory"] as? String ?: "",
                                productImageUrl = it["productImageUrl"] as? String ?: "",
                                color = it["color"] as? String ?: "",
                                size = it["size"] as? String ?: "",
                                date = it["date"] as? Long ?: 0,
                                transactionMethod = it["transactionMethod"] as? String ?: "",
                                transactionId = it["transactionId"] as? String ?: "",
                                email = it["email"] as? String ?: "",
                                contactNumber = it["contactNumber"] as? String ?: "",
                                fullName = it["fullName"] as? String ?: "",
                                address = it["address"] as? String ?: "",
                                status = it["status"] as? String ?: "Processing"
                            )
                            allOrders.add(order)
                        }
                    }
                }
                trySend(ResultState.Success(allOrders))
                close()
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.message ?: "Failed to fetch orders"))
                close()
            }
        awaitClose { }
    }

    override fun createRazorpayOrder(amount: Double, userId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        
        // Log to verify what we are sending
        android.util.Log.d("OrderCreate", "Requesting Order - Amount: ${amount.toInt()}, UserID: '$userId'")

        val request = com.wp7367.myshoppingapp.data_layer.remote.PaymentOrderRequest(amount.toInt(), userId)
        val job = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val response = paymentApiService.createOrder(request)
                if (response.success) {
                    trySend(ResultState.Success(response.data.orderId))
                } else {
                    trySend(ResultState.Error(response.message))
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                // Parsing the error message from backend if possible
                val errorMessage = try {
                    val json = org.json.JSONObject(errorBody ?: "")
                    json.optString("message", "Validation Error (422)")
                } catch (_: Exception) {
                    "Server Error: ${e.code()}"
                }
                android.util.Log.e("OrderCreate", "HTTP Error: ${e.code()} - $errorBody")
                trySend(ResultState.Error(errorMessage))
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message ?: "Network error"))
            } finally {
                close()
            }
        }
        awaitClose { job.cancel() }
    }

    override fun verifyPaymentOnBackend(
        paymentId: String,
        orderId: String,
        signature: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Log the data being sent to debug HTTP 500
        android.util.Log.d("PaymentVerify", "Sending to Backend - OrderID: $orderId, PaymentID: $paymentId")

        if (orderId.isEmpty()) {
            trySend(ResultState.Error("Razorpay Order ID is missing. Please create an order on backend first."))
            close()
            return@callbackFlow
        }

        val request = PaymentVerificationRequest(
            orderId = orderId,
            paymentId = paymentId,
            signature = signature
        )

        val job = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val response = paymentApiService.verifyPayment(request)
                if (response.success) {
                    trySend(ResultState.Success("Payment Verified"))
                } else {
                    trySend(ResultState.Error(response.message ?: "Verification failed"))
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                android.util.Log.e("PaymentVerify", "HTTP Error: ${e.code()} - $errorBody")
                trySend(ResultState.Error("Server Error (500): Check Backend Logs"))
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message ?: "Network error"))
            } finally {
                close()
            }
        }

        awaitClose { job.cancel() }
    }




    //   ~ Fcm Push Notification ~

    private fun updateFcmToken(userId: String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if (it.isSuccessful){
                val token = it.result
                FirebaseFirestore.collection("USER_TOKEN").document(userId).set(mapOf("token" to token))
            }
        }
    }




}
