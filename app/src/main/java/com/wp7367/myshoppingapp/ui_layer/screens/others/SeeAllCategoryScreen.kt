package com.wp7367.myshoppingapp.ui_layer.screens.others

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllCategoryScreen(
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {
    val ctState by viewModel.getAllCategory.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAllCategory()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("All Categories", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F8F8))
        ) {
            when {
                ctState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                ctState.data.isEmpty() -> {
                    Text(
                        text = "No categories found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(ctState.data) { data ->
                            data?.let { categoryData ->
                                CategoryGridItem(
                                    category = categoryData,
                                    onClick = {
                                        // Industry level: navigate back to Home and filter, or go to a dedicated category products screen
                                        // For now, consistent with Home behavior: set search query
                                        // But since we are on a separate screen, maybe we should pass it back or navigate to SeeAllProduct with filter
                                        // Let's assume navigating to SeeAllProduct with category would be better, but the user wants it like Home.
                                        // Actually, navigating to a Search result screen is better.
                                        // For simplicity, we can navigate back with a result or just navigate to SeeAllProduct.
                                        navController.previousBackStackEntry?.savedStateHandle?.set("selected_category", categoryData.name)
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(category: category, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = category.imageUri,
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
