package com.wp7367.myshoppingapp.data_layer.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class PaymentVerificationRequest(
    val orderId: String,
    val paymentId: String,
    val signature: String
)

data class PaymentVerificationResponse(
    val status: String,
    val message: String? = null
)

interface PaymentApiService {
    @POST("verify") // This assumes your endpoint is /verify
    suspend fun verifyPayment(
        @Body request: PaymentVerificationRequest
    ): PaymentVerificationResponse
}
