package com.wp7367.myshoppingapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Keep this import
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUi(viewModels: MyViewModel = hiltViewModel(),navController: NavController) {


    //  -- Here State is Collect --

    val ctState = viewModels.getAllCategory.collectAsState()              // ~ Combine Two State in Single State ~
    val productState = viewModels.getAllProduct.collectAsState()






    LaunchedEffect(key1 = Unit) {
        viewModels.getAllCategory()
        viewModels.getAllProduct()
    }


// ------------------- ~ New UI ~ ------------------------------------------------------------------









    Scaffold() { innerPadding ->
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
                    value = "",
                    onValueChange = {

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

                items(ctState.value.data ?: emptyList()) { data ->
                    CategoryItem(
                        ImageUri = data!!.imageUri,
                        Category = data.name,
                        onClick = {


                        })


                }
            }
               Spacer(modifier = Modifier.height(8.dp))

//        Banner

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 145.dp, max = 159.dp)
                    .padding(9.dp)


            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(4.dp), // Padding for content inside banner
                    contentAlignment = Alignment.Center,

                    ) {
                    Text(
                        text = "Banner",
                        fontSize = 32.sp,
                        color = Color.Blue,
                    )
                }

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
                )
            }





            LazyRow( modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp), // Reduced from 10.dp
                horizontalArrangement = Arrangement.spacedBy(10.dp) // Reduced from 12.dp
            )
            {

                items(productState.value.data ?:emptyList()) { product ->

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









