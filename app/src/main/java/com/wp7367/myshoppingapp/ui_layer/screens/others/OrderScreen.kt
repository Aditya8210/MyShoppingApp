package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.orderModel
import com.wp7367.myshoppingapp.ui_layer.viewModel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderScreen(navController: NavController, orderViewmodel: OrderViewModel) {


    val getAllOrderState = orderViewmodel.getAllOrderDataState.collectAsState()

    LaunchedEffect(Unit) {
        orderViewmodel.getAllOrderState()

    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                navController.navigateUp()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Text(
                text = "Return to Profile", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Gray,
                )
            )
        }
        Spacer(Modifier.height(16.dp))

        when {
            getAllOrderState.value.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            getAllOrderState.value.data != null -> {
                val orderList = getAllOrderState.value.data
                if (orderList!!.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(orderList) { order ->
                            OrderView(orderModel = order)
                        }
                    }
                    Log.d("@order", "OrderScreen: ")
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No orders found.")
                    }
                }
            }

            getAllOrderState.value.error.isNotEmpty() -> {
                Toast.makeText(context, getAllOrderState.value.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun OrderView(orderModel: orderModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = orderModel.productImageUrl,
                contentDescription = orderModel.productName, // Added content description
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp) 
                    .clip(MaterialTheme.shapes.medium), // Used medium shape
                contentScale = ContentScale.Crop
            )


            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Order ID: ${orderModel.orderId}", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "product Name: ${orderModel.productName}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Category: ${orderModel.productCategory}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Total Price: ${orderModel.productFinalPrice}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${formatDate(orderModel.date)}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Status: ${orderModel.status}")

            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
