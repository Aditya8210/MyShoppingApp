package com.wp7367.myshoppingapp.ui_layer.screens.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.R
import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel




@Composable
fun SignUpScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController){

//    ~ Here State is Collect ~

    val context = LocalContext.current
    val authState by viewModel.registerUser.collectAsStateWithLifecycle()



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
            navController.navigate(Routes.HomeScreen)
        }
    }

    //    Screen Ui


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle332212),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .align(Alignment.TopEnd)
                .graphicsLayer {
                    translationX = size.width / 4
                    translationY = -size.height / 4
                }
        )

        Image(
            painter = painterResource(id = R.drawable.circle33221),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f)
                .align(Alignment.BottomStart)
                .graphicsLayer {
                    translationX = -size.width / 4
                    translationY = size.height / 4
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 135.dp),
        ) {
            Text(
                text = "Create Account:",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 60.dp)

            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastUseName.value,
                    onValueChange = { lastUseName.value = it },
                    label = { Text("LastName") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Gmail") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("PASSWORD") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    label = { Text("PHONE NUMBER") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
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
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = "Register")
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
    return when {
        firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || phoneNumber.isBlank() -> (false to "All fields are required.")
        !isValidEmail(email) -> (false to "Invalid email format.")
        password.length < 6 -> (false to "Password must be at least 6 characters long.")
        phoneNumber.length < 10 -> (false to "Phone number must be at least 10 digits.")
        else -> (true to "")
    }
}
