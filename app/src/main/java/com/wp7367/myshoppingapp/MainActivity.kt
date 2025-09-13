package com.wp7367.myshoppingapp
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.wp7367.myshoppingapp.ui.theme.MyShoppingAppTheme
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.AppNav
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener{                   // Step - 3
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNav(firebaseAuth)
                }
            }
        }


        // Step - 4

        Checkout.preload(applicationContext)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("rzp_live_XXXXXXXXXXXXXX")



    }



        // Step - 5 and Remove the Private Key , and give parameters to startPayment

   fun startPayment(

       amount: Int,
       name : String,

   ) {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name",name)
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",amount)//pass amount in currency subunits

            val retryObj =  JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","<email>")
            prefill.put("contact","<phone>")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onPaymentSuccess(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(errorCode: Int, description: String?) {
        Toast.makeText(this, "We appreciate your patience. While we fix this, continue browsing on the app.", Toast.LENGTH_LONG).show()
        // You might want to log the error code and description for debugging
         Log.e("PaymentError", "Code: $errorCode Description: $description")
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