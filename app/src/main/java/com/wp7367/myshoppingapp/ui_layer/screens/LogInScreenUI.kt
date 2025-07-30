package com.wp7367.myshoppingapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.wp7367.myshoppingapp.domain_layer.models.userData

@Composable
fun  LoginScreen(viewModel: MyViewModel = hiltViewModel()){

    val lgState = viewModel.loginUser.collectAsState()


    val context = LocalContext.current

    when{
        lgState.value.isLoading ->{
            CircularProgressIndicator()

        }
        lgState.value.error != null ->{
            Toast.makeText(context,lgState.value.error,Toast.LENGTH_SHORT).show()

        }
        lgState.value.data != null ->{
            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()

        }



    }

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){



        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }




        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Gmail") }

        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("PASSWORD") }

        )



        Button(onClick = {

            viewModel.loginUser(email.value,password.value)
        }) {
            Text(text = "Login")

        }


    }

}