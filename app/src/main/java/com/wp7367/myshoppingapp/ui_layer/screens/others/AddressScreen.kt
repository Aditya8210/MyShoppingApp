package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.ShippingViewModel

@Composable
fun AddressScreen(navController: NavController, shippingViewModel: ShippingViewModel= hiltViewModel()) {


    val AddressState by shippingViewModel.getShippingAd.collectAsStateWithLifecycle()

    val DeleteAddressSt by shippingViewModel.deleteAddressSt.collectAsStateWithLifecycle()

    val context = LocalContext.current



    LaunchedEffect(DeleteAddressSt) {
            if (DeleteAddressSt.data != null) {
                Toast.makeText(context, "Address Deleted", Toast.LENGTH_SHORT).show()

            }
        shippingViewModel.getShippingDataById()
    }


   when{
       AddressState.isLoading ->{
           CircularProgressIndicator()
       }
       AddressState.error != null ->{

       }
       AddressState.data != null ->{

           AddressData(ShippingAddress = AddressState.data.firstOrNull() ?: shippingModel(),navController)
       }
   }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color.White)

                .padding(start =16.dp, end = 16.dp) // Adjusted padding
        ) {

            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Address:",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Continue Shopping",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }


            Spacer(modifier = Modifier.height(5.dp))

            //Show Shipping Address

            AddressData(ShippingAddress = AddressState.data.firstOrNull() ?: shippingModel(), navController)
        }
    }



}


@Composable
fun AddressData(ShippingAddress: shippingModel,navController: NavController,viewModel: ShippingViewModel= hiltViewModel()) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Added vertical padding
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Address:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    // Replace with dynamic address data
                    Text(text = ShippingAddress.fullName, style = MaterialTheme.typography.bodyLarge)
                    Text(text = ShippingAddress.address1, style = MaterialTheme.typography.bodyMedium)
                    Text(text = ShippingAddress.address2, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${ShippingAddress.city}, ${ShippingAddress.state}, ${ShippingAddress.postalCode}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = ShippingAddress.contactNumber, style = MaterialTheme.typography.bodyMedium)
                }


                IconButton(onClick =
                    {
                        navController.navigate(Routes.EditAddressScreen)


                    }, modifier = Modifier.padding(end = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EditNote,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(47.dp)
                    )
                }
                Spacer(modifier = Modifier.width(3.dp))

                IconButton(onClick =
                    {
                       viewModel.deleteAddress(ShippingAddress)


                    }, modifier = Modifier.padding(end = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }



}



