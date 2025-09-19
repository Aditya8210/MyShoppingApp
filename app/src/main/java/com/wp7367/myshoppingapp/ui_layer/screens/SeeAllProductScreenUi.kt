package com.wp7367.myshoppingapp.ui_layer.screens



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement // Added import
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells // Added import
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes


@Composable
fun SeeAllProductUi(modifier: Modifier = Modifier,viewModels: MyViewModel = hiltViewModel(),navController: NavController) {


    val allProductState = viewModels.getAllProduct.collectAsState()


    LaunchedEffect(key1 = Unit) {

        viewModels.getAllProduct()

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
                IconButton(onClick = {  navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "All Product",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Continue Shopping",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextField(value = "Search", onValueChange = {"search"},

                    placeholder = { Text(text = "Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                )

            }
            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Added columns
                horizontalArrangement = Arrangement.spacedBy(12.dp), // Added horizontal arrangement
                verticalArrangement = Arrangement.spacedBy(15.dp), // Added vertical arrangement
                modifier = Modifier.fillMaxWidth() // Added fillMaxWidth for better layout
            ) {
                items(allProductState.value.data?:emptyList()) { product ->
                    AllProduct(product = product!!, onClick = {
                        navController.navigate(Routes.EachProductDetailScreen(product.productId))
                    })
                }

            }

        }
    }
}

@Composable
fun AllProduct(product: ProductModel, onClick: () -> Unit){
    // It's good practice for items in a grid to align themselves within their cell.
    // Consider wrapping this in a Column with appropriate alignment if needed.
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
