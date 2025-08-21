package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreenUi(viewModel: MyViewModel = hiltViewModel(),navController: NavController,productId: String ) {

    val checkOutScreenSt = viewModel.getProductById.collectAsState()



    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("free") }




    LaunchedEffect(key1 = Unit){
       viewModel.getProductById(productId)

    }

    when{
        checkOutScreenSt.value.isLoading ->{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }

        }
        checkOutScreenSt.value.error != null ->{
            Text(text = checkOutScreenSt.value.error!!)

        }
        checkOutScreenSt.value.data != null ->{


//            Column {
//                Text(checkOutScreenSt.value.data!!.name)
//            }

            Scaffold (modifier = Modifier.fillMaxSize()){innerPadding ->        // Apply Scaffold to get Extra Space


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(10.dp)
                        .verticalScroll(scrollState)                   // Apply After innerPadding then it work
                ) {
                    // Top bar
                    Text(
                        text = "Shipping",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Cart Item
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                           AsyncImage(
                               model = checkOutScreenSt.value.data!!.image,
                               contentDescription = null,
                               modifier = Modifier
                                   .size(80.dp)
                                   .clip(RoundedCornerShape(8.dp))
                           )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(checkOutScreenSt.value.data!!.name, style = MaterialTheme.typography.bodyLarge)
                                Text("UK10", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹=" + checkOutScreenSt.value.data!!.finalPrice, style = MaterialTheme.typography.displayMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Contact Information
                    Text("Contact Information:", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Shipping Address
                    Text("Shipping Address:", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = { Text("PinCode") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contactNumber,
                        onValueChange = { contactNumber = it },
                        label = { Text("Contact Number") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Shipping Method
                    Text("Shipping Method:", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = "free" }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedMethod == "free",
                            onClick = { selectedMethod = "free" }
                        )
                        Text("Standard FREE delivery over ₹4500", modifier = Modifier.padding(start = 8.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Free")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = "cod" }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedMethod == "cod",
                            onClick = { selectedMethod = "cod" }
                        )
                        Text("Cash on Delivery over ₹500 (Free Delivery, COD processing ₹50)",
                            modifier = Modifier.padding(start = 8.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text("₹100")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Continue Button
                    Button(
                        onClick = { /* Handle navigation */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continue to Shipping")
                    }
                }



            }




        }
    }


}