package com.wp7367.myshoppingapp.ui_layer.screens.others

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.util.Patterns

@Composable
fun EditAddressScreen(modifier: Modifier = Modifier, navController: NavController) {

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var address1 by remember { mutableStateOf("") }
    var address2 by remember { mutableStateOf("") } // Optional field, might not need strict validation
    var city by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var address1Error by remember { mutableStateOf<String?>(null) }
    var cityError by remember { mutableStateOf<String?>(null) }
    var pinCodeError by remember { mutableStateOf<String?>(null) }
    var contactNumberError by remember { mutableStateOf<String?>(null) }
    var stateError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    fun validateForm(): Boolean {
        var isValid = true

        // Email validation
        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        // Full Name validation
        if (fullName.isBlank()) {
            fullNameError = "Full name cannot be empty"
            isValid = false
        } else {
            fullNameError = null
        }

        // Address1 validation
        if (address1.isBlank()) {
            address1Error = "Address line 1 cannot be empty"
            isValid = false
        } else {
            address1Error = null
        }

        // City validation
        if (city.isBlank()) {
            cityError = "City cannot be empty"
            isValid = false
        } else {
            cityError = null
        }

        // PinCode validation
        if (pinCode.isBlank()) {
            pinCodeError = "Pin code cannot be empty"
            isValid = false
        } else if (pinCode.any { !it.isDigit() } || pinCode.length != 6) {
            pinCodeError = "Invalid pin code (must be 6 digits)"
            isValid = false
        } else {
            pinCodeError = null
        }

        // Contact Number validation
        if (contactNumber.isBlank()) {
            contactNumberError = "Contact number cannot be empty"
            isValid = false
        } else if (contactNumber.any { !it.isDigit() } || contactNumber.length < 10) { // Basic check for digits and length
            contactNumberError = "Invalid contact number"
            isValid = false
        } else {
            contactNumberError = null
        }

        // State validation
        if (state.isBlank()) {
            stateError = "State cannot be empty"
            isValid = false
        } else {
            stateError = null
        }
        
        return isValid
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Address",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Continue Shopping",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Contact Information:", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it 
                    emailError = null 
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { 
                    contactNumber = it
                    contactNumberError = null
                },
                label = { Text("Contact Number") },
                modifier = Modifier.fillMaxWidth(),
                isError = contactNumberError != null,
                supportingText = { contactNumberError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Shipping Address:", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = fullName,
                onValueChange = { 
                    fullName = it 
                    fullNameError = null
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = fullNameError != null,
                supportingText = { fullNameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = address1,
                onValueChange = { 
                    address1 = it 
                    address1Error = null
                },
                label = { Text("House No, Building Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = address1Error != null,
                supportingText = { address1Error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = address2,
                onValueChange = { address2 = it }, // No specific error state for address2, can be optional
                label = { Text("RoadName, Area,Colony (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { 
                    city = it 
                    cityError = null
                },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                isError = cityError != null,
                supportingText = { cityError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = pinCode,
                onValueChange = { 
                    pinCode = it 
                    pinCodeError = null
                },
                label = { Text("PinCode") },
                modifier = Modifier.fillMaxWidth(),
                isError = pinCodeError != null,
                supportingText = { pinCodeError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = state,
                onValueChange = { 
                    state = it 
                    stateError = null
                },
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth(),
                isError = stateError != null,
                supportingText = { stateError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validateForm()) {
                        // Proceed with saving logic
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        }
    }
}
