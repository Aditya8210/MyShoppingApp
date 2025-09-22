package com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items // Keep this import
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.ui_layer.screens.others.AllProduct
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreenUi(viewModels: MyViewModel = hiltViewModel(), navController: NavController, modifier: Modifier = Modifier) {


    //  -- Here State is Collect --

    val ctState by viewModels.getAllCategory.collectAsStateWithLifecycle()              // ~Optional Also Combine Two State in Single State ~
    val productState by viewModels.getAllProduct.collectAsStateWithLifecycle()

    val bannerSate by viewModels.getBanner.collectAsStateWithLifecycle()

    val searchQueryState by viewModels.searchProduct.collectAsStateWithLifecycle()

    val searchQuery = remember { mutableStateOf("") }








    LaunchedEffect(key1 = Unit) {
        viewModels.getAllCategory()
        viewModels.getAllProduct()
        viewModels.getBanner()
        viewModels.searchQuery()

    }


// ------------------- ~ New UI ~ ------------------------------------------------------------------


    Scaffold()
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        )

        {

            // Top Bar: Search and Notification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery.value,
                    onValueChange = {
                        searchQuery.value = it
                        viewModels.onSearchQueryChange(it)
                    },
                    placeholder = { Text(text = "Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),


                    )
                IconButton(onClick = {})
                {
                    Icon(Icons.Default.NotificationAdd, contentDescription = null)
                }
            }

            if (searchQuery.value.isNotEmpty() && !searchQueryState.data.isNullOrEmpty()) {


                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Added columns
                    horizontalArrangement = Arrangement.spacedBy(12.dp), // Added horizontal arrangement
                    verticalArrangement = Arrangement.spacedBy(15.dp), // Added vertical arrangement
                    modifier = Modifier.fillMaxWidth() // Added fillMaxWidth for better layout
                ) {
                    items(searchQueryState.data ?: emptyList()) { product ->
                        AllProduct(product = product!!, onClick = {
                            navController.navigate(Routes.EachProductDetailScreen(product.productId))
                        })
                    }

                }

            }

           else
         {


            // Category Title Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Category",
                    fontStyle = FontStyle.Normal,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "See More",
                    fontSize = 14.sp,
                    color = Color.Magenta,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp), // Overall padding for the row
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Adjusted spacing
            )
            {

                items(ctState.data ?: emptyList()) { data ->
                    CategoryItem(
                        ImageUri = data!!.imageUri,
                        Category = data.name,
                        onClick = {


                        })


                }
            }
            Spacer(modifier = Modifier.height(8.dp))

//        Banner

            Box(
                modifier = modifier // Changed Column to Box
                    .fillMaxWidth()
                    .heightIn(min = 145.dp, max = 170.dp) // Adjusted max height for indicator
                    .padding(9.dp)
            )
            {

                val pagerState = rememberPagerState(initialPage = 0)

                // Auto-scroll effect
                LaunchedEffect(key1 = bannerSate.data?.size) { // Observe banner data size for changes
                    val pageCount = bannerSate.data?.size ?: 0
                    if (pageCount > 0) { // ensure there are pages to scroll
                        while (true) {
                            delay(3000) // 3-second delay
                            val nextPage = (pagerState.currentPage + 1) % pageCount
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                }

                HorizontalPager(
                    count = bannerSate.data?.size ?: 0,
                    state = pagerState,
                    itemSpacing = 20.dp, // Changed from pageSpacing to itemSpacing for Accompanist
                    modifier = Modifier.fillMaxWidth()
                ) { page -> // page is the index in Accompanist
                    AsyncImage(
                        model = bannerSate.data?.get(page),
                        contentDescription = "Banner Image",
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = pagerState.pageCount, // Use pagerState.pageCount
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = Color.LightGray
                )
            }


            // Flash Sale Title Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "FLASH SALE",
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "See More",
                    fontSize = 14.sp,
                    color = Color.Magenta,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SeeAllProduct)
                    }
                )
            }





            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp), // Reduced from 10.dp
                horizontalArrangement = Arrangement.spacedBy(10.dp) // Reduced from 12.dp
            )
            {

                items(productState.data ?: emptyList()) { product ->

                    ProductCard(
                        product = product!!,

                        onClick = {
                            navController.navigate(Routes.EachProductDetailScreen(product.productId))

                        })

                }


            }

         }
        }

    }
 }











// ----------------   ~ Old UI ~ -------------------------------------------------------------------

//    Column(modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        LazyRow {
//            //  here check proper items  datatype like List
//            items(ctState.value.data){
//
//                Text(text = it!!.name)
//                Text(text = it.imageUri)
//
//            }
//        }
//    }

//--------------------------------------------------------------------------------------------------



@Composable
fun CategoryItem(
    ImageUri: String,
    Category: String,
    onClick: () -> Unit

){
    Column(
        modifier = Modifier
            .widthIn(min = 90.dp, max = 120.dp) // Flexible width
            .padding(vertical = 8.dp)  ,         // Added vertical padding
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .size(70.dp)
                .clickable(onClick = onClick),
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.Black)
        ) {
            AsyncImage(
                model = ImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize() // Image fills the card
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = Category,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ProductCard(product: ProductModel, onClick: () -> Unit) {


    Column(
        modifier = Modifier
            .padding(top = 8.dp) // Keep top padding, horizontal managed by LazyRow
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card (
            modifier = Modifier
                .width(145.dp)
                .wrapContentHeight()                   // Let card height adjust to content
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(20.dp)

        ){
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()   // Image takes full width of the Column
                    .heightIn(min = 70.dp, max = 110.dp), // Fixed height for image consistency
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.height(8.dp))


            Text(text = product.name, modifier = Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = product.category, modifier = Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text ="Price ${product.price}", modifier = Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Final Price - ${product.finalPrice}" , modifier = Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.height(2.dp))



        }

      }
}









