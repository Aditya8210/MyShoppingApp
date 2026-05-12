package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EachProductDetailScreen(productId: String, viewModels: MyViewModel = hiltViewModel(), navController: NavController, ) {

    val eachProductDetailSt by viewModels.getProductById.collectAsStateWithLifecycle()

    val setCartItemState by viewModels.setCartItem.collectAsStateWithLifecycle()
    val setFavSt by viewModels.setFavItem.collectAsStateWithLifecycle()
    val context = LocalContext.current // Get context for Toast
    val scope = rememberCoroutineScope()


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
    LaunchedEffect(setCartItemState) {
        val state = setCartItemState
        if (state.data != null && !state.isLoading && state.error == null) {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
        } else if (state.error != null && !state.isLoading) {
            Toast.makeText(context, "Error: ${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(setFavSt) {
        val state = setFavSt
        if (state.data != null && !state.isLoading && state.error == null) {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
        } else if (state.error != null && !state.isLoading) {
            Toast.makeText(context, "Error adding to wishlist: ${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    when {
        eachProductDetailSt.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        eachProductDetailSt.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = eachProductDetailSt.error!!, color = Color.Red)
            }
        }

        eachProductDetailSt.data != null -> {
            val product = eachProductDetailSt.data!!
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 8.dp,
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
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
                                            productId = product.productId,
                                            name = product.name,
                                            imageUrl = product.image,
                                            price = product.finalPrice,
                                            quantity = quantity,
                                            size = selectedSize ?: "",
                                            color = selectedColor?.let { "0x" + Integer.toHexString(it.hashCode()) } ?: "",
                                            availableUnits = product.availableUnits
                                        )
                                        viewModels.setCartItem(data)
                                    } else {
                                        scope.launch {
                                            scrollState.animateScrollTo(500) // Scroll to selection area
                                        }
                                    }
                                },
                                enabled = product.availableUnits > 0,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, if (product.availableUnits > 0) Color.DarkGray else Color.LightGray)
                            ) {
                                Text("Add to Cart", color = if (product.availableUnits > 0) Color.DarkGray else Color.LightGray, fontWeight = FontWeight.Bold)
                            }

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
                                        navController.navigate(
                                            Routes.CheckOutScreen(
                                                productId = productId,
                                                quantity = quantity,
                                                size = selectedSize ?: "",
                                                color = selectedColor?.let { "0x" + Integer.toHexString(it.hashCode()) } ?: ""
                                            )
                                        )
                                    } else {
                                        scope.launch {
                                            scrollState.animateScrollTo(500) // Scroll to selection area
                                        }
                                    }
                                },
                                enabled = product.availableUnits > 0,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE57373),
                                    disabledContainerColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (product.availableUnits > 0) "Buy Now" else "Out of Stock", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient overlay for buttons visibility
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Transparent,
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                onClick = { navController.popBackStack() },
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = "Back",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Black
                                    )
                                }
                            }

                            Surface(
                                onClick = {
                                    val favData = favouriteModel(
                                        productId = product.productId,
                                        name = product.name,
                                        image = product.image,
                                        finalPrice = product.finalPrice,
                                        category = product.category,
                                        description = product.description,
                                        availableUnits = product.availableUnits
                                    )
                                    viewModels.setFavItem(favData)
                                },
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Wishlist",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp), // Adjust this if you want it to overlap
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = product.category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "₹${product.finalPrice}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFE57373)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < 4) Color(0xFFFFD700) else Color.LightGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "(4.5/5)",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(Modifier.height(24.dp))

                            Text(
                                "Select Size",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (sizeError != null) Color.Red else Color.Unspecified
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .then(
                                        if (sizeError != null) Modifier.border(1.dp, Color.Red, RoundedCornerShape(8.dp)).padding(4.dp)
                                        else Modifier
                                    )
                            ) {
                                val sizes = when {
                                    product.category.contains("shoe", ignoreCase = true) -> listOf("UK 7", "UK 8", "UK 9", "UK 10")
                                    product.category.contains("jean", ignoreCase = true) -> listOf("28", "30", "32", "34")
                                    product.category.contains("cloth", ignoreCase = true) || 
                                    product.category.contains("shirt", ignoreCase = true) || 
                                    product.category.contains("t-shirt", ignoreCase = true) -> listOf("S", "M", "L", "XL", "XXL")
                                    else -> listOf("S", "M", "L", "XL")
                                }
                                sizes.forEach { size ->
                                    val isSelected = selectedSize == size
                                    Surface(
                                        onClick = {
                                            selectedSize = size
                                            sizeError = null
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) Color(0xFFE57373) else Color.White,
                                        border = BorderStroke(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFE57373) else Color.LightGray
                                        ),
                                        modifier = Modifier.size(width = 65.dp, height = 45.dp),
                                        shadowElevation = if (isSelected) 4.dp else 0.dp
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = size,
                                                color = if (isSelected) Color.White else Color.Black,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                            sizeError?.let {
                                Text(
                                    text = "⚠️ $it",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Text(
                                "Select Color",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (colorError != null) Color.Red else Color.Unspecified
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .then(
                                        if (colorError != null) Modifier.border(1.dp, Color.Red, RoundedCornerShape(8.dp)).padding(4.dp)
                                        else Modifier
                                    )
                            ) {
                                listOf(
                                    Color(0xFFF53131),
                                    Color(0xFF1A36E7),
                                    Color(0xFF1E7507),
                                    Color(0xFF212121),
                                    Color(0xFFFFC107),
                                    Color(0xFF9C27B0)
                                ).forEach { color ->
                                    val isSelected = selectedColor == color
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .border(
                                                width = if (isSelected) 3.dp else 1.dp,
                                                color = if (isSelected) Color.Black else Color.LightGray.copy(alpha = 0.5f),
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                selectedColor = color
                                                colorError = null
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            colorError?.let {
                                Text(
                                    text = "⚠️ $it",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("Quantity", fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (quantity > 1) quantity-- },
                                        enabled = product.availableUnits > 0,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }
                                    Text(
                                        if (product.availableUnits > 0) quantity.toString() else "0",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    IconButton(
                                        onClick = { 
                                            if (quantity < product.availableUnits) quantity++
                                            else {
                                                Toast.makeText(context, "Only ${product.availableUnits} units available", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        enabled = product.availableUnits > 0 && quantity < product.availableUnits,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }
                            }

                            if (product.availableUnits > 0 && product.availableUnits <= 5) {
                                Text(
                                    "Only ${product.availableUnits} items left!",
                                    color = Color(0xFFE57373),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            } else if (product.availableUnits <= 0) {
                                Text(
                                    "Out of Stock",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Spacer(Modifier.height(24.dp))

                            Text(
                                "Description",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                lineHeight = 20.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Delivery in 3-5 Business Days",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(80.dp)) // Extra space for bottom bar
                        }
                    }
                }
            }
        }
    }
}
