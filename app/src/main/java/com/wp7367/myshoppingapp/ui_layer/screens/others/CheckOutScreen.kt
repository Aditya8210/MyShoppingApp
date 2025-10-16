package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Added for LazyColumn items extension
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.MainActivity
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel
import com.wp7367.myshoppingapp.ui_layer.viewModel.OrderViewModel
import com.wp7367.myshoppingapp.ui_layer.viewModel.ShippingViewModel


@Composable
fun CheckOutScreenUi(
    viewModel: MyViewModel = hiltViewModel(),
    shippingViewmodel: ShippingViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    navController: NavController,
    productId: String? // Nullable for cart checkout
) {


    // ~Here State is Collect~

    val productState by viewModel.getProductById.collectAsStateWithLifecycle()
    val cartState by viewModel.getCartItem.collectAsStateWithLifecycle()

    val addressDataState by shippingViewmodel.getShippingAd.collectAsStateWithLifecycle()

    val orderState = orderViewModel.orderState.collectAsStateWithLifecycle()







    var selectedPaymentMethod by remember { mutableStateOf("Online payment") }
    val paymentOptions = listOf("Online payment", "Cash on delivery")

    val context = LocalContext.current
    val activity = context as? MainActivity      //step -6



    LaunchedEffect(key1 = productId) { // Re-trigger if productId changes
        if (productId != null) {
            viewModel.getProductById(productId)
        } else {

            viewModel.getCartItem() // Optional: if you want to ensure fresh data for cart checkout
        }
        shippingViewmodel.getShippingDataById()
    }

    LaunchedEffect(orderState.value.data, orderState.value.error) {
        val data = orderState.value.data
        val error = orderState.value.error

        when {
            data.isNotEmpty() -> {
                Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
//                navController.navigate(Routes.OrderConfirmationScreen)
            }

            error.isNotEmpty() -> {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    val isLoading = if (productId != null) productState.isLoading else cartState.isLoading
    val error = if (productId != null) productState.error else cartState.error
    val productData = productState.data
    val cartData = cartState.data

    val currentTotalAmount: Double = remember(productId, productData, cartData) {
        if (productId != null) {
            productData?.finalPrice?.toDoubleOrNull() ?: 0.0
        } else {
            cartData?.sumOf {
                (it?.price?.toDoubleOrNull() ?: 0.0) * (it?.quantity ?: 1)
            } ?: 0.0
        }
    }

    val primaryItemName: String = remember(productId, productData, cartData) {
        if (productId != null) {
            productData?.name ?: "Product"
        } else {
            cartData?.firstOrNull()?.name ?: "Your Order"
        }
    }





    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $error")
            }
        }

        else -> {
            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color.White)

                        .padding(16.dp) // Adjusted padding
                ) {

                    // Top Bar
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 17.dp)) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Payment:",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Continue Shopping",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    //Show Shipping Address

                    AddressScreen(ShippingAddress = addressDataState.data.firstOrNull() ?: shippingModel(), navController)



                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Shopping List",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Dynamic Shopping List
                    if (productId != null) { // Direct Buy
                        if (productData != null) {
                            ProductItem(productItem = productData)
                        } else {
                            Text("Product details not available.")
                        }
                    } else { // Cart Checkout
                        if (!cartData.isNullOrEmpty()) {
                            LazyColumn(
                                modifier = Modifier.weight(1f, fill = false), // Allow column to scroll if content exceeds space
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cartData.filterNotNull(), key = { cartItem -> cartItem.productId ?: cartItem.hashCode() }) { item ->
                                    GetCartItem(cardItem = item)
                                }
                            }
                        } else {
                            Text("Your cart is empty.")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Shipping Method",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)) {
                        Column(modifier = Modifier.padding(13.dp)) {
                            paymentOptions.forEach { paymentOption ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedPaymentMethod = paymentOption }
                                        .padding(vertical = 5.dp) // Adjusted padding
                                ) {
                                    RadioButton(
                                        selected = (selectedPaymentMethod == paymentOption),
                                        onClick = { selectedPaymentMethod = paymentOption }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = paymentOption, style = MaterialTheme.typography.bodyLarge)
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = "₹${"%.2f".format(currentTotalAmount)}", // Formatted total
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (currentTotalAmount <= 0) {
                                Toast.makeText(context, "Cannot proceed with zero amount.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                     //    For razorpay :Step 7  and Also Changes in Manifest Like Meta-Data

                            if (selectedPaymentMethod == "Online payment") {
                                val amountInPaise = (currentTotalAmount * 100).toInt()
                                activity?.startPayment(
                                    amount = amountInPaise,
                                    name = primaryItemName
                                )
                            } else {// Cash on Delivery
                                val shippingAddress = addressDataState.data.firstOrNull()
                                val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

                                if (shippingAddress == null) {
                                    Toast.makeText(context, "No shipping address found", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                // Create Order List
                                val orderList = if (productId != null && productData != null) {
                                    listOf(
                                        orderModel(
                                            orderId = "",
                                            productId = productData.productId ?: "",
                                            productName = productData.name,
                                            productDescription = productData.description ?: "",
                                            productQty = "1",
                                            productFinalPrice = productData.finalPrice ?: "0",
                                            productCategory = productData.category ?: "",
                                            productImageUrl = productData.image ?: "",
                                            color = "",
                                            size = "",
                                            transactionMethod = selectedPaymentMethod,
                                            transactionId = "",
                                            email = userEmail,
                                            contactNumber = shippingAddress.contactNumber,
                                            fullName = shippingAddress.fullName,
                                            address = "${shippingAddress.address1}, ${shippingAddress.city}, ${shippingAddress.state} - ${shippingAddress.postalCode}",
                                            status = "",
                                        )
                                    )
                                } else {
                                    createOrderListFromCart(
                                        cartList = cartData?.filterNotNull() ?: emptyList(),
                                        address = shippingAddress,
                                        paymentMethod = selectedPaymentMethod
                                    )
                                }

                                // ✅ Save Order
                                orderViewModel.orderDataSave(orderList)

                                // ✅ Optional: Show message and navigate after success
                                Toast.makeText(context, "Placing Order...", Toast.LENGTH_SHORT).show()


                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp) // Adjusted height
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = if (selectedPaymentMethod == "Cash on delivery") "Order Now" else "Pay Now (₹${"%.2f".format(currentTotalAmount)})",
                            color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

fun createOrderListFromCart(
    cartList: List<cartItemModel>,
    address: shippingModel,
    paymentMethod: String
): List<orderModel> {
    return cartList.map { cartItem ->
        orderModel(
            orderId = "",
            productId = cartItem.productId ?: "",
            productName = cartItem.name,
            productQty = cartItem.quantity.toString(),
            productFinalPrice = cartItem.price ?: "0",
            productCategory = cartItem.name ?: "",
            productImageUrl = cartItem.imageUrl ?: "",
//            color = cartItem.color ?: "",
//            size = cartItem.size ?: "",
            transactionMethod = paymentMethod,
            transactionId = "",
            email = address.email,
            contactNumber = address.contactNumber,
            fullName = address.fullName,
            address = "${address.address1},${address.address1}, ${address.city}, ${address.state} - ${address.postalCode}",
            status = "Pending"
        )
    }
}


@Composable
fun GetCartItem(cardItem: cartItemModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = cardItem.imageUrl,
                contentDescription = cardItem.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)) // Placeholder background
                , contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(cardItem.name, style = MaterialTheme.typography.titleMedium, maxLines = 2) // Using titleMedium
                // Assuming size or other details might be here
                // Text("Size: ${cardItem.size ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                 Text(
                    "₹${cardItem.price?.toDoubleOrNull()?.let { "%.2f".format(it) } ?: "N/A"}", //Formatted Price
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) // Using bodyLarge
                )
            }
            // Optional: Quantity, remove button etc.
        }
    }
}


@Composable
fun ProductItem(productItem: ProductModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = productItem.image,
                contentDescription = productItem.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)) // Placeholder background
                , contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(productItem.name, style = MaterialTheme.typography.titleMedium, maxLines = 2) // Using titleMedium

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "₹${productItem.finalPrice?.toDoubleOrNull()?.let { "%.2f".format(it) } ?: "N/A"}", // Formatted Price
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) // Using bodyLarge
                )
            }
        }
    }
}

@Composable
fun AddressScreen(ShippingAddress: shippingModel,navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Added vertical padding
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Address:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    // Replace with dynamic address data
                    Text(text = ShippingAddress.fullName, style = MaterialTheme.typography.bodyLarge)
                    Text(text = ShippingAddress.address1, style = MaterialTheme.typography.bodyMedium)
                    Text(text = ShippingAddress.address2, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${ShippingAddress.city}, ${ShippingAddress.state}, ${ShippingAddress.postalCode}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = ShippingAddress.contactNumber, style = MaterialTheme.typography.bodyMedium)
                }


                IconButton(onClick =
                    {
                        navController.navigate(Routes.EditAddressScreen)


                    }, modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EditNote,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        }
    }



}
