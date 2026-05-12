package com.wp7367.myshoppingapp
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.viewModels
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.wp7367.myshoppingapp.ui.theme.MyShoppingAppTheme
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.AppNav
import com.wp7367.myshoppingapp.ui_layer.viewModel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNav(firebaseAuth)
                    }
                }
            }
        }

        Checkout.preload(applicationContext)
    }

    fun startPayment(
        amount: Int,
        name: String,
        email: String,
        contact: String,
        razorpayOrderId: String = "",
        description: String = "Purchase from MyShoppingApp"
    ) {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", name)
            options.put("description", description)
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", amount)

            if (razorpayOrderId.isNotEmpty()) {
                options.put("order_id", razorpayOrderId)
            }

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("contact", contact)

            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        Log.d("PaymentSuccess", "Payment ID: $razorpayPaymentId")
        // Pass PaymentData which contains paymentId, orderId, and signature
        orderViewModel.setPaymentState(
            paymentId = razorpayPaymentId ?: "Success",
            signature = paymentData?.signature ?: "",
            razorpayOrderId = paymentData?.orderId ?: ""
        )
    }

    override fun onPaymentError(errorCode: Int, description: String?, paymentData: PaymentData?) {
        Log.e("PaymentError", "Code: $errorCode Description: $description")
        orderViewModel.setPaymentState(errorMsg = description ?: "Payment Failed")
        Toast.makeText(this, "Payment Failed: $description", Toast.LENGTH_LONG).show()
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyShoppingAppTheme {
//        Greeting("Android")
//    }
//}