package com.wp7367.myshoppingapp.data_layer.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class PaymentVerificationRequest(
    val orderId: String,
    val paymentId: String,
    val signature: String
)

data class PaymentVerificationResponse(
    val success: Boolean, // Changed from status: String to match backend
    val message: String? = null
)

data class PaymentOrderRequest(
    val amount: Int, // Changed to Int to match backend expectation
    val userId: String
)

// Matching the screenshot response structure
data class PaymentOrderResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: OrderData
)

data class OrderData(
    val orderId: String,
    val amount: Int
)

interface PaymentApiService {
    @POST("external-order") // Path from screenshot
    suspend fun createOrder(
        @Body request: PaymentOrderRequest
    ): PaymentOrderResponse

    @POST("verify-external")
    suspend fun verifyPayment(
        @Body request: PaymentVerificationRequest
    ): PaymentVerificationResponse
}
