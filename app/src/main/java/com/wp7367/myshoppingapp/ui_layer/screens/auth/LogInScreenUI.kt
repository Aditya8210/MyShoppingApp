package com.wp7367.myshoppingapp.ui_layer.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.R
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel


@Composable
fun LoginScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController) {

    val lgState by viewModel.loginUser.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


        when {
            lgState.error != null -> {
                showToast(context, lgState.error!!)
            }

            lgState.data != null -> {
                showToast(context, "Login Successfully")
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }


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
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


           Text(
                text = "LOGIN",
                modifier = Modifier.padding(bottom = 24.dp),
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = Color.Blue,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Gray.copy(alpha = 0.5f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    ),
                    letterSpacing = 3.sp,
                    fontFamily = FontFamily.Cursive
                )
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Gmail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable {
                            showToast(context, "Forgot Password")
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isValidEmail(email) && password.length > 5) {
                        viewModel.loginUser(email, password)
                    } else if (!isValidEmail(email)) {
                        showToast(context, "Invalid Email")
                    } else {
                        showToast(context, "Password must be at least 6 characters long")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !lgState.isLoading
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(text = "Don't have An Account? ")
                Text(
                    text = "Sign Up",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignUpScreen)
                    }
                )
            }
        }

        if (lgState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}