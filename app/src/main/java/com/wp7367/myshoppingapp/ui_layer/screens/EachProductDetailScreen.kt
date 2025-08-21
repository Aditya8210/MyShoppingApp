package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachProductDetailScreen(productId: String ,viewModels: MyViewModel = hiltViewModel(), navController: NavController) {

    val EachProductDetailSt = viewModels.getProductById.collectAsState()



    val scrollState = rememberScrollState()
    var quantity by remember { mutableStateOf(1) }
    var selectedSize by remember { mutableStateOf("Uk 10") }
    var selectedColor by remember { mutableStateOf(Color(0xFFE57373)) }


    LaunchedEffect(key1 = Unit){
       viewModels.getProductById(productId)

    }

    when{
        EachProductDetailSt.value.isLoading ->{

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }
        }
        EachProductDetailSt.value.error!= null ->{
            Text(text = EachProductDetailSt.value.error!!)

        }
        EachProductDetailSt.value.data != null ->{


//            Column (modifier = Modifier.fillMaxSize())
//            {
//
//                Text(text = EachProductDetailSt.value.data!!.name)
//            }

//-----------------------------------------------------------------------------------------------------------------------------

            Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->


            Column (modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(scrollState))
            {

                // --- Product Image ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(370.dp)
                ) {
                   AsyncImage(
                       model = EachProductDetailSt.value.data!!.image,
                       contentDescription = null,
                       contentScale = ContentScale.Crop,
                       modifier = Modifier.fillMaxSize()
                   )
                    // Back Icon
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp))
                    {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }

                }


                // --- Product Details ---
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = EachProductDetailSt.value.data!!.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp)) // Add some space
                    Text( // Display Category
                        text = "Category: ${EachProductDetailSt.value.data!!.category}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Rating
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < 3) Color(0xFFFFD700) else Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                // Price
                Text(
                    text = "Price: ${EachProductDetailSt.value.data!!.finalPrice}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Size selector
                Spacer(Modifier.height(8.dp))
                Text("Size", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                    listOf("UK 8", "UK 7", "UK 9").forEach { size ->
                        OutlinedButton(
                            onClick = { selectedSize = size},
                            border = BorderStroke(1.dp, if (selectedSize == size) Color.Red else Color.Gray),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = if (selectedSize == size) Color.Red else Color.Black)
                        ) {
                            Text(size)
                        }
                    }
                }

                // Quantity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(quantity.toString(), fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }

                // Color selector
                Spacer(Modifier.height(12.dp))
                Text("Color", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                    listOf(
                        Color(0xFFF53131),
                        Color(0xFF1A36E7),
                        Color(0xFF1E7507),
                        Color(0xFF0F866F)
                    ).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    2.dp,
                                    if (selectedColor == color) Color.Black else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                // Specification
                Spacer(Modifier.height(12.dp))
                Text("Specification :", fontWeight = FontWeight.Bold)
                Text(EachProductDetailSt.value.data!!.description, fontSize = 14.sp)
                Text(
                    "Please bear in mind that the photo may be slightly different from the actual item...",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                }

                // Buttons
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Routes.CheckOutScreen(productId)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                        .padding(start = 22.dp, end = 22.dp)
                ) {
                    Text("Buy now", color = Color.White)
                }

                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                        .padding(start = 22.dp, end = 22.dp)
                ) {
                    Text("Add to Cart", color = Color.White)
                }

                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(start = 22.dp, end = 22.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Wishlist", tint = Color.Red)
                    Spacer(Modifier.width(4.dp))
                    Text("Add to Wishlist", color = Color.Red)
                }
            }

            }

        }
    }

}