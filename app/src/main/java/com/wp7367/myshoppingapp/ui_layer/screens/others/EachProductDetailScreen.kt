package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachProductDetailScreen(productId: String, viewModels: MyViewModel = hiltViewModel(), navController: NavController,


                            ) {

    val eachProductDetailSt by viewModels.getProductById.collectAsStateWithLifecycle()

    val setCartItemState = viewModels.setCartItem.collectAsState()
    val setFavSt = viewModels.setFavItem.collectAsState()
    val context = LocalContext.current // Get context for Toast



    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var selectedColor by remember { mutableStateOf<Color?>(null) } // Initialize to null
    var sizeError by remember { mutableStateOf<String?>(null) }
    var colorError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit){
       viewModels.getProductById(productId)
    }


    // for show Toast
    LaunchedEffect(setCartItemState.value) {
        val state = setCartItemState.value
        if (state.data != null && !state.isLoading && state.error == null) {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
        } else if (state.error != null && !state.isLoading) {
            Toast.makeText(context, "Error: ${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(setFavSt.value) {
        val state = setFavSt.value
        if (state.data != null && !state.isLoading && state.error == null) {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
        } else if (state.error != null && !state.isLoading) {
            Toast.makeText(context, "Error adding to wishlist: ${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    when{
        eachProductDetailSt.isLoading ->{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        eachProductDetailSt.error!= null ->{
            Text(text = eachProductDetailSt.error!!)
        }
        eachProductDetailSt.data != null ->{
            Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
                Column (modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .verticalScroll(scrollState))
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(370.dp)
                    ) {
                       AsyncImage(
                           model = eachProductDetailSt.data!!.image,
                           contentDescription = null,
                           contentScale = ContentScale.Crop,
                           modifier = Modifier.fillMaxSize()
                       )
                        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp))
                        {
                            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = eachProductDetailSt.data!!.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Category: ${eachProductDetailSt.data!!.category}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
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
                        Text(
                            text = "Price: ${eachProductDetailSt.data!!.finalPrice}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(8.dp))
                        Text("Size", fontWeight = FontWeight.Medium)
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                                listOf("UK 8", "UK 7", "UK 9").forEach { size ->
                                    OutlinedButton(
                                        onClick = {
                                            selectedSize = size
                                            sizeError = null
                                        },
                                        border = BorderStroke(1.dp, if (selectedSize == size) Color.Red else Color.Gray),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = if (selectedSize == size) Color.Red else Color.Black)
                                    ) {
                                        Text(size)
                                    }
                                }
                            }
                            sizeError?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(quantity.toString(), fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            IconButton(onClick = { quantity++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Text("Color", fontWeight = FontWeight.Medium)
                        Column { // Wrap Row and Error in a Column for color selector
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
                                            .clickable { 
                                                selectedColor = color 
                                                colorError = null
                                            }
                                    )
                                }
                            }
                            colorError?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Text("Specification :", fontWeight = FontWeight.Bold)
                        Text(eachProductDetailSt.data!!.description, fontSize = 14.sp)
                        Text(
                            "Please bear in mind that the photo may be slightly different from the actual item...",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            var isValid = true
                            if (selectedSize == null) {
                                sizeError = "Please select a size."
                                isValid = false
                            }
                            if (selectedColor == null) {
                                colorError = "Please select a color."
                                isValid = false
                            }
                            if (isValid) {
                                navController.navigate(Routes.CheckOutScreen(productId))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                            .padding(start = 22.dp, end = 22.dp)
                    ) {
                        Text("Buy now", color = Color.White)
                    }

                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            var isValid = true
                            if (selectedSize == null) {
                                sizeError = "Please select a size."
                                isValid = false
                            }
                             if (selectedColor == null) {
                                colorError = "Please select a color."
                                isValid = false
                            }

                            if (isValid) {
                                val data = cartItemModel(
                                    productId = eachProductDetailSt.data!!.productId,
                                    name = eachProductDetailSt.data!!.name,
                                    imageUrl = eachProductDetailSt.data!!.image,
                                    price = eachProductDetailSt.data!!.finalPrice,
                                    quantity = quantity,
                                    // size = selectedSize, // Ensure cartItemModel has a 'size' field
                                    // color = selectedColor?.value.toString() // Ensure cartItemModel has a 'color' field
                                )
                                viewModels.setCartItem(data)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                            .padding(start = 22.dp, end = 22.dp)
                    ) {
                        Text("Add to Cart", color = Color.White)
                    }

                    TextButton(
                        onClick = {
                            val favData = favouriteModel(
                                productId = eachProductDetailSt.data!!.productId,
                                name = eachProductDetailSt.data!!.name,
                                image = eachProductDetailSt.data!!.image,
                                finalPrice = eachProductDetailSt.data!!.finalPrice,
                                category = eachProductDetailSt.data!!.category,
                                description = eachProductDetailSt.data!!.description,
                            )
                            viewModels.setFavItem(favData)
                        },
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
