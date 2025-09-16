package com.wp7367.myshoppingapp.ui_layer.screens.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.ui_layer.screens.MyViewModel

@Composable
fun CartPage(modifier: Modifier = Modifier, viewModels: MyViewModel = hiltViewModel(), navController: NavController) {

    val cardState = viewModels.getCartItem.collectAsState()



    LaunchedEffect(Unit) {
        viewModels.getCartItem()


    }


    Scaffold { innerPadding ->
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
            .padding(16.dp)
    ) {

        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Continue Shopping",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cart Items List
        LazyColumn (modifier = Modifier.fillMaxWidth().weight(1f)) // Added weight to allow subtotal to be at bottom
        {
           items(cardState.value.data ?: emptyList()){  cardData ->
               cardData?.let { // Safe call for potentially null item
                   CardItem(
                       cartItem = it,
                       modifier = Modifier
                   )
               }
           }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Added spacer before subtotal

        // Subtotal Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sub Total", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("Rs: ${(cardState.value.data ?: emptyList()).sumOf { item -> (item?.price?.toIntOrNull() ?: 0) * (item?.quantity ?: 0) }}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }

        // Checkout Button
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38E07B))
        ) {
            Text("Checkout", color = Color.White)
        }
    }
    }
}

@Composable
fun CardItem(
    viewModels: MyViewModel= hiltViewModel(),
   cartItem: cartItemModel,
   modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(model = cartItem.imageUrl,
                contentDescription = cartItem.name, // Added content description
                modifier = Modifier
                    .size(100.dp) // Slightly reduced size
                    .clip(MaterialTheme.shapes.medium), // Used medium shape
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) { // Added weight to take available space
                Text(
                    text = cartItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold // Added bold for name
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Price: Rs ${cartItem.price}", // Clarified price label
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                 Text(
                    text = "Qty: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text("Total: Rs ${(cartItem.price.toIntOrNull() ?: 0) * cartItem.quantity}", 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = {

                    viewModels.deleteCartItem(cartItem)

                }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Remove item")
                }
            }
        }
    }
}