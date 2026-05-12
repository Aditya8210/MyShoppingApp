package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.domain_layer.models.shippingModel
import com.wp7367.myshoppingapp.ui_layer.viewModel.ShippingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ShippingViewModel = hiltViewModel()
) {
    val editAddressState by viewModel.shippingAddressSt.collectAsStateWithLifecycle()
    val existingAddressState by viewModel.getShippingAd.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var address1 by remember { mutableStateOf("") }
    var address2 by remember { mutableStateOf("") }
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

    // Fetch existing address
    LaunchedEffect(Unit) {
        viewModel.getShippingDataById()
    }

    // Pre-fill fields if address exists
    LaunchedEffect(existingAddressState.data) {
        val existing = existingAddressState.data.firstOrNull()
        if (existing != null) {
            email = existing.email
            fullName = existing.fullName
            address1 = existing.address1
            address2 = existing.address2
            city = existing.city
            pinCode = existing.postalCode
            contactNumber = existing.contactNumber
            state = existing.state
        }
    }

    LaunchedEffect(editAddressState) {
        if (editAddressState.data != null) {
            Toast.makeText(context, "Address Saved Successfully", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            navController.popBackStack()
        }
        if (editAddressState.error != null) {
            Toast.makeText(context, editAddressState.error, Toast.LENGTH_SHORT).show()
        }
    }

    fun validateForm(): Boolean {
        var isValid = true
        if (email.isBlank()) { emailError = "Required"; isValid = false } 
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailError = "Invalid Email"; isValid = false } 
        else { emailError = null }

        if (fullName.isBlank()) { fullNameError = "Required"; isValid = false } else { fullNameError = null }
        if (address1.isBlank()) { address1Error = "Required"; isValid = false } else { address1Error = null }
        if (city.isBlank()) { cityError = "Required"; isValid = false } else { cityError = null }
        if (state.isBlank()) { stateError = "Required"; isValid = false } else { stateError = null }

        if (pinCode.isBlank()) { pinCodeError = "Required"; isValid = false } 
        else if (pinCode.length != 6 || !pinCode.all { it.isDigit() }) { pinCodeError = "Invalid (6 digits)"; isValid = false } 
        else { pinCodeError = null }

        if (contactNumber.isBlank()) { contactNumberError = "Required"; isValid = false } 
        else if (contactNumber.length < 10 || !contactNumber.all { it.isDigit() }) { contactNumberError = "Invalid (10 digits)"; isValid = false } 
        else { contactNumberError = null }

        return isValid
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Shipping Address", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = {
                        if (validateForm()) {
                            val addressData = shippingModel(
                                email = email,
                                fullName = fullName,
                                address1 = address1,
                                address2 = address2,
                                city = city,
                                postalCode = pinCode,
                                contactNumber = contactNumber,
                                state = state,
                            )
                            viewModel.shippingAddress(addressData)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                ) {
                    Text("Save Address", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(0xFFF8F8F8))) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AddressSectionCard(title = "Contact Details", icon = Icons.Default.Info) {
                    AddressTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = "Email Address",
                        error = emailError,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                    AddressTextField(
                        value = contactNumber,
                        onValueChange = { contactNumber = it; contactNumberError = null },
                        label = "Phone Number",
                        error = contactNumberError,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                }

                AddressSectionCard(title = "Shipping Details", icon = Icons.Default.LocationOn) {
                    AddressTextField(
                        value = fullName,
                        onValueChange = { fullName = it; fullNameError = null },
                        label = "Full Name",
                        error = fullNameError,
                        imeAction = ImeAction.Next
                    )
                    AddressTextField(
                        value = address1,
                        onValueChange = { address1 = it; address1Error = null },
                        label = "House No, Building Name",
                        error = address1Error,
                        imeAction = ImeAction.Next
                    )
                    AddressTextField(
                        value = address2,
                        onValueChange = { address2 = it },
                        label = "Area, Colony, Road Name (Optional)",
                        imeAction = ImeAction.Next
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            AddressTextField(
                                value = city,
                                onValueChange = { city = it; cityError = null },
                                label = "City",
                                error = cityError,
                                imeAction = ImeAction.Next
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            AddressTextField(
                                value = pinCode,
                                onValueChange = { pinCode = it; pinCodeError = null },
                                label = "Pincode",
                                error = pinCodeError,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        }
                    }
                    AddressTextField(
                        value = state,
                        onValueChange = { state = it; stateError = null },
                        label = "State",
                        error = stateError,
                        imeAction = ImeAction.Done
                    )
                }
                Spacer(modifier = Modifier.height(80.dp))
            }

            if (editAddressState.isLoading || existingAddressState.isLoading) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE57373))
                }
            }
        }
    }
}

@Composable
fun AddressSectionCard(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFFE57373), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun AddressTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE57373),
                focusedLabelColor = Color(0xFFE57373),
                cursorColor = Color(0xFFE57373)
            ),
            singleLine = true
        )
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
        }
    }
}
