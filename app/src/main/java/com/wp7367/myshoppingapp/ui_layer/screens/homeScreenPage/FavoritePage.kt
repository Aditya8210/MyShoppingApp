package com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel

@Composable
fun FavoritePage(modifier: Modifier = Modifier,viewModels: MyViewModel = hiltViewModel(), navController: NavController) {

        val favoriteState = viewModels.getFavItem.collectAsState()


        Scaffold { innerPadding ->
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(innerPadding)
                                .padding(8.dp)
                )
                {
                        // Top Bar
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back"
                                        )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = " My WishList",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                        text = "Continue Shopping",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium
                                )
                        }

                        if (favoriteState.value.data.isEmpty()) {
                                Box(
                                        modifier = Modifier
                                                .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text("Your wishlist is empty 😢", fontSize = 18.sp)
                                }
                        } else {
                                LazyColumn(
                                        modifier = Modifier
                                                .fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(14.dp),
                                        contentPadding = PaddingValues(8.dp)
                                ) {

                                        items(favoriteState.value.data ?: emptyList()){ favItem ->
                                                favItem?.let {
                                                        FavItem(favItem = it, viewModels = viewModels)
                                                }

                                        }


                                }

                        }


                }
        }

}

@Composable
fun FavItem(favItem: favouriteModel, viewModels: MyViewModel= hiltViewModel())
{
      Card (
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(18.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
      ){

            Row (
                 modifier = Modifier
                         .fillMaxWidth()
                         .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface,
                                                 MaterialTheme.colorScheme.surfaceVariant)))
                         .padding(14.dp),
                 verticalAlignment = Alignment.CenterVertically
            ){

                 // Product Image

                AsyncImage(
                        model = favItem.image,
                        contentDescription = null,
                        modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                )
                    Spacer(modifier = Modifier.width(14.dp))


                    // Product Info
                    Column(
                            modifier = Modifier.weight(1f)
                    ) {
                            Text(
                                    text = favItem.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                    text = favItem.category,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                    text = "₹${favItem.finalPrice}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                    onClick = {    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(38.dp)
                            ) {
                                    Text("Move to Cart")
                            }
                    }
                    // Delete Button
                    IconButton(onClick = {
                            viewModels.deleteFavItem(favItem)
                    }) {
                            Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.error
                            )
                    }
            }


      }


}

