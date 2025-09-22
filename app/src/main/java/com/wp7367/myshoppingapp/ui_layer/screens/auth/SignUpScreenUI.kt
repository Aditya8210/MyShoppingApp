package com.wp7367.myshoppingapp.ui_layer.screens.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.R
import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel




@Composable
fun SignUpScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController){

//    ~ Here State is Collect ~

    val context = LocalContext.current
    val authState by viewModel.registerUser.collectAsStateWithLifecycle()

    // ----- Media Query style breakpoints -----
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val fieldWidth: Dp
    val buttonWidth: Dp
    val circleBig: Dp
    val circleSmall: Dp
    val circleBigOffsetX: Dp
    val circleBigOffsetY: Dp
    val circleSmallOffsetY: Dp

    when {
        screenWidth < 360.dp -> {
            // Small phone
            fieldWidth = 260.dp
            buttonWidth = 200.dp
            circleBig = 160.dp
            circleSmall = 140.dp
            circleBigOffsetX = 120.dp
            circleBigOffsetY = (-40).dp
            circleSmallOffsetY = 20.dp
        }
        screenWidth < 600.dp -> {
            // Normal phone
            fieldWidth = 320.dp
            buttonWidth = 250.dp
            circleBig = 220.dp
            circleSmall = 180.dp
            circleBigOffsetX = 210.dp
            circleBigOffsetY = (-60).dp
            circleSmallOffsetY = 30.dp
        }
        else -> {
            // Tablet / Foldable
            fieldWidth = 400.dp
            buttonWidth = 300.dp
            circleBig = 300.dp
            circleSmall = 260.dp
            circleBigOffsetX = 250.dp
            circleBigOffsetY = (-80).dp
            circleSmallOffsetY = 40.dp
        }
    }

    //  ~ Here State is Manage ~
    when{
        authState.isLoading ->{
            CircularProgressIndicator()
        }
        authState.error != null ->{
            Toast.makeText(context,authState.error.toString(),Toast.LENGTH_SHORT).show()
        }
        authState.data != null ->{
            Toast.makeText(context,authState.data.toString(),Toast.LENGTH_SHORT).show()
            // Consider navigating away here, e.g., navController.popBackStack() or to a login/home screen
        }
    }

    //    Screen Ui
    Column (modifier = Modifier
                  .fillMaxSize()
                  .fillMaxWidth()
                  .background(color = Color.White),)
    {

        Row(
            modifier = Modifier.fillMaxWidth())
        {
            Box(
                modifier = Modifier
                    .size(circleBig)
                    .offset(x = circleBigOffsetX, y = circleBigOffsetY)
                    .align(Alignment.Top)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.circle332212),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())
            }
        }

        Text(text = "Create Account:", modifier = Modifier.padding(start = 15.dp),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            val firstUserName = remember { mutableStateOf("") }
            val lastUseName = remember { mutableStateOf("") }
            val email = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }
            val phoneNumber = remember { mutableStateOf("") }

            OutlinedTextField(
                value = firstUserName.value,
                onValueChange = { firstUserName.value = it },
                label = { Text("FirstName") },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = lastUseName.value,
                onValueChange = { lastUseName.value = it },
                label = { Text("LastName") },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Gmail") },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("PASSWORD") },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = {phoneNumber.value = it },
                label = { Text("PHONE NUMBER") },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                val (isValid, message) = validateInputs(
                    firstUserName.value,
                    lastUseName.value,
                    email.value,
                    password.value,
                    phoneNumber.value
                )
                if (isValid) {
                    val data = userData(
                        firstName = firstUserName.value,
                        lastName = lastUseName.value,
                        email = email.value,
                        password = password.value,
                        phoneNumber = phoneNumber.value
                    )
                    viewModel.registerUser(data)
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },modifier = Modifier.width(buttonWidth)) {
                Text(text = "Register")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .size(circleSmall)
                    .offset(y = circleSmallOffsetY)
                    .align(Alignment.Bottom)
                ){
                    Image(painter = painterResource(id = R.drawable.circle33221),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun validateInputs(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    phoneNumber: String
): Pair<Boolean, String> {
    if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || phoneNumber.isBlank()) {
        return Pair(false, "All fields are required.")
    }
    if (!isValidEmail(email)) {
        return Pair(false, "Invalid email format.")
    }
    if (password.length < 6) {
        return Pair(false, "Password must be at least 6 characters long.")
    }
    // Add more specific validation for phone number if needed
    return Pair(true, "")
}
