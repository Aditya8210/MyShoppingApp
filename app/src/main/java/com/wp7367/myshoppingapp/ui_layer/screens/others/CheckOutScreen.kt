package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreenUi(
    viewModel: MyViewModel = hiltViewModel(),
    shippingViewmodel: ShippingViewModel = hiltViewModel(),
    navController: NavController,
    productId: String?,
    quantity: Int = 1,
    size: String = "",
    color: String = ""
) {
    val context = LocalContext.current

    // ~~ Step 6 ~~ Razorpay
    val activity = context.findActivity()
    
    
    
    val orderViewModel: OrderViewModel = hiltViewModel(activity ?: (context as ComponentActivity))

    val productState by viewModel.getProductById.collectAsStateWithLifecycle()
    val cartState by viewModel.getCartItem.collectAsStateWithLifecycle()
    val addressDataState by shippingViewmodel.getShippingAd.collectAsStateWithLifecycle()
    val orderState by orderViewModel.orderState.collectAsStateWithLifecycle()
    val paymentState by orderViewModel.paymentState.collectAsStateWithLifecycle()
    val createOrderState by orderViewModel.createOrderState.collectAsStateWithLifecycle()

    val currentSelectedAddress = remember(addressDataState.data) {
        addressDataState.data.find { it.selected } ?: addressDataState.data.firstOrNull() ?: shippingModel()
    }

    val productData = productState.data
    val cartData = cartState.data
    val isLoading = (if (productId != null) productState.isLoading else cartState.isLoading) || 
                    createOrderState.isLoading || 
                    orderState.isLoading
    val error = if (productId != null) productState.error else cartState.error

    var selectedPaymentMethod by remember { mutableStateOf("Online payment") }
    val paymentOptions = listOf("Online payment", "Cash on delivery")

    val scrollState = rememberScrollState()

    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.getProductById(productId)
        } else {
            viewModel.getCartItem()
        }
    }

    LaunchedEffect(Unit) {
        shippingViewmodel.getShippingDataById()
    }

    LaunchedEffect(paymentState) {
        if (paymentState.paymentId.isNotEmpty()) {
            val shippingAddress = currentSelectedAddress
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

            if (shippingAddress.fullName.isNotEmpty()) {
                val orderList = if (productId != null && productData != null) {
                    val totalPrice = (productData.finalPrice.toDoubleOrNull() ?: 0.0) * quantity
                    listOf(
                        orderModel(
                            productId = productData.productId,
                            productName = productData.name,
                            productDescription = productData.description,
                            productQty = quantity.toString(),
                            productFinalPrice = "%.2f".format(totalPrice),
                            productCategory = productData.category,
                            productImageUrl = productData.image,
                            color = color,
                            size = size,
                            transactionMethod = "Online payment",
                            transactionId = paymentState.paymentId,
                            email = userEmail,
                            contactNumber = shippingAddress.contactNumber,
                            fullName = shippingAddress.fullName,
                            address = "${shippingAddress.address1}, ${shippingAddress.city}, ${shippingAddress.state} - ${shippingAddress.postalCode}",
                            status = "Processing", // Will be updated to Paid after verification
                        )
                    )
                } else {
                    createOrderListFromCart(
                        cartList = cartData.filterNotNull(),
                        address = shippingAddress,
                        paymentMethod = "Online payment"
                    ).map { it.copy(transactionId = paymentState.paymentId, status = "Processing") }
                }

                // Call Backend Verification
                orderViewModel.verifyAndSaveOrder(
                    paymentId = paymentState.paymentId,
                    orderId = paymentState.razorpayOrderId,
                    signature = paymentState.signature,
                    orderList = orderList
                )
                orderViewModel.clearPaymentState()
            }
        } else if (paymentState.error.isNotEmpty()) {
            Toast.makeText(context, paymentState.error, Toast.LENGTH_SHORT).show()
            orderViewModel.clearPaymentState()
        }
    }

    LaunchedEffect(orderState.data, orderState.error) {
        if (orderState.data.isNotEmpty()) {
            navController.navigate(Routes.OrderSuccess) {
                popUpTo(Routes.HomeScreen) { inclusive = false }
            }
            orderViewModel.clearOrderState()
        } else if (orderState.error.isNotEmpty()) {
            Toast.makeText(context, orderState.error, Toast.LENGTH_SHORT).show()
            orderViewModel.clearOrderState()
        }
    }

    val primaryItemName: String = remember(productId, productData, cartData) {
        if (productId != null) {
            productData?.name ?: "Product"
        } else {
            cartData.firstOrNull()?.name ?: "Your Order"
        }
    }

    val currentTotalAmount: Double = remember(productId, productData, cartData, quantity) {
        if (productId != null) {
            (productData?.finalPrice?.toDoubleOrNull() ?: 0.0) * quantity
        } else {
            cartData.sumOf {
                (it?.price?.toDoubleOrNull() ?: 0.0) * (it?.quantity ?: 1)
            }
        }
    }

    LaunchedEffect(createOrderState) {
        if (createOrderState.orderId.isNotEmpty()) {
            val shippingAddress = currentSelectedAddress
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
            (activity as? MainActivity)?.startPayment(
                amount = (currentTotalAmount * 100).toInt(),
                name = primaryItemName,
                email = userEmail,
                contact = shippingAddress.contactNumber,
                razorpayOrderId = createOrderState.orderId,
                description = "Order for $primaryItemName"
            )
            orderViewModel.clearCreateOrderState()
        } else if (createOrderState.error.isNotEmpty()) {
            Toast.makeText(context, createOrderState.error, Toast.LENGTH_SHORT).show()
            orderViewModel.clearCreateOrderState()
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE57373))
            }
        }

        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $error", color = Color.Red)
            }
        }

        else -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "Checkout",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                bottomBar = {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 8.dp,
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "Total Amount",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        "₹${"%.2f".format(currentTotalAmount)}",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        val shippingAddress = currentSelectedAddress
                                        if (shippingAddress.fullName.isEmpty()) {
                                            Toast.makeText(context, "Please add a shipping address", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }

                                        if (currentTotalAmount <= 0) {
                                            Toast.makeText(context, "Total amount is zero", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }

                                        if (selectedPaymentMethod == "Online payment") {
                                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                            orderViewModel.createRazorpayOrder(currentTotalAmount, userId)
                                        } else {
                                            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
                                            val orderList = if (productId != null && productData != null) {
                                                val totalPrice = (productData.finalPrice.toDoubleOrNull() ?: 0.0) * quantity
                                                listOf(
                                                    orderModel(
                                                        productId = productData.productId,
                                                        productName = productData.name,
                                                        productDescription = productData.description,
                                                        productQty = quantity.toString(),
                                                        productFinalPrice = "%.2f".format(totalPrice),
                                                        productCategory = productData.category,
                                                        productImageUrl = productData.image,
                                                        color = color,
                                                        size = size,
                                                        transactionMethod = "Cash on delivery",
                                                        email = userEmail,
                                                        contactNumber = shippingAddress.contactNumber,
                                                        fullName = shippingAddress.fullName,
                                                        address = "${shippingAddress.address1}, ${shippingAddress.city}, ${shippingAddress.state} - ${shippingAddress.postalCode}",
                                                        status = "Pending",
                                                    )
                                                )
                                            } else {
                                                createOrderListFromCart(cartData.filterNotNull(), shippingAddress, "Cash on delivery")
                                            }
                                            orderViewModel.orderDataSave(orderList)
                                        }
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(180.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                                ) {
                                    Text(
                                        text = if (selectedPaymentMethod == "Cash on delivery") "Place Order" else "Pay Now",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color(0xFFF8F8F8))
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    SectionHeader(title = "Shipping Address", icon = Icons.Default.LocationOn)
                    AddressScreen(
                        shippingAddress = currentSelectedAddress,
                        navController
                    )

                    SectionHeader(title = "Order Items", icon = Icons.AutoMirrored.Filled.ReceiptLong)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (productId != null) {
                            productData?.let { ProductItem(productItem = it, quantity = quantity, size = size, color = color) }
                                ?: Text("Product details not available.")
                        } else {
                            if (cartData.isNotEmpty()) {
                                cartData.filterNotNull().forEach { GetCartItem(cardItem = it) }
                            } else {
                                Text("Your cart is empty.")
                            }
                        }
                    }

                    SectionHeader(title = "Payment Method", icon = Icons.Default.Payment)
                    PaymentMethodSection(
                        options = paymentOptions,
                        selectedOption = selectedPaymentMethod,
                        onOptionSelected = { selectedPaymentMethod = it },
                        amount = currentTotalAmount
                    )

                    SectionHeader(title = "Bill Details", icon = Icons.AutoMirrored.Filled.ReceiptLong)
                    BillDetailsSection(totalAmount = currentTotalAmount)

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFE57373),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }
}

@Composable
fun PaymentMethodSection(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    amount: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(option) }
                        .padding(12.dp)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onOptionSelected(option) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFE57373)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(Modifier.weight(1f))
                    if (isSelected) {
                        Text(
                            text = "₹${"%.2f".format(amount)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE57373)
                        )
                    }
                }
                if (option != options.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun BillDetailsSection(totalAmount: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BillRow(label = "Subtotal", value = "₹${"%.2f".format(totalAmount)}")
            BillRow(label = "Shipping Fee", value = "FREE", valueColor = Color(0xFF4CAF50))
            BillRow(label = "Discount", value = "- ₹0.00", valueColor = Color(0xFF4CAF50))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Payable",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${"%.2f".format(totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE57373)
                )
            }
        }
    }
}

@Composable
fun BillRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun createOrderListFromCart(
    cartList: List<cartItemModel>,
    address: shippingModel,
    paymentMethod: String
): List<orderModel> {
    return cartList.map { cartItem ->
        val totalPrice = (cartItem.price.toDoubleOrNull() ?: 0.0) * cartItem.quantity
        orderModel(
            productId = cartItem.productId,
            productName = cartItem.name,
            productQty = cartItem.quantity.toString(),
            productFinalPrice = "%.2f".format(totalPrice),
            productCategory = "", // Category info isn't in cartItemModel, could be added if needed
            productImageUrl = cartItem.imageUrl,
            color = cartItem.color,
            size = cartItem.size,
            transactionMethod = paymentMethod,
            email = address.email,
            contactNumber = address.contactNumber,
            fullName = address.fullName,
            address = "${address.address1}, ${address.city}, ${address.state} - ${address.postalCode}",
            status = "Pending"
        )
    }
}

@Composable
fun GetCartItem(cardItem: cartItemModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cardItem.imageUrl,
                contentDescription = cardItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    cardItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Qty: ${cardItem.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    if (cardItem.size.isNotEmpty()) {
                        Text(
                            " | Size: ${cardItem.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (cardItem.color.isNotEmpty()) {
                        Text(
                            " | ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(parseColor(cardItem.color))
                                .border(0.5.dp, Color.LightGray, CircleShape)
                        )
                    }
                }
                Text(
                    "₹${"%.2f".format((cardItem.price.toDoubleOrNull() ?: 0.0) * cardItem.quantity)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFFE57373)
                )
            }
        }
    }
}

fun parseColor(colorString: String): Color {
    return try {
        if (colorString.startsWith("0x")) {
            Color(colorString.substring(2).toLong(16))
        } else {
            Color(android.graphics.Color.parseColor(colorString))
        }
    } catch (e: Exception) {
        Color.Transparent
    }
}

@Composable
fun ProductItem(productItem: ProductModel, quantity: Int = 1, size: String = "", color: String = "") {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = productItem.image,
                contentDescription = productItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    productItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${productItem.category}${if(size.isNotEmpty()) " | Size: $size" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    if (color.isNotEmpty()) {
                        Text(
                            " | ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(parseColor(color))
                                .border(0.5.dp, Color.LightGray, CircleShape)
                        )
                    }
                }
                Text(
                    "Qty: $quantity",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    "₹${"%.2f".format((productItem.finalPrice.toDoubleOrNull() ?: 0.0) * quantity)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFFE57373)
                )
            }
        }
    }
}

@Composable
fun AddressScreen(shippingAddress: shippingModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE57373).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFE57373)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (shippingAddress.fullName.isNotEmpty()) {
                    Text(
                        text = shippingAddress.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${shippingAddress.address1}, ${shippingAddress.city}, ${shippingAddress.state} - ${shippingAddress.postalCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Phone: ${shippingAddress.contactNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    Column {
                        Text(
                            text = "No shipping address found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Please add your delivery address",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            IconButton(
                onClick = { navController.navigate(Routes.AddressScreen) },
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFF5F5F5), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Edit",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
