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


fun SignUpScreen(viewModel: MyViewModel = hiltViewModel()){

//    ~ Here State is Collect ~

    val context = LocalContext.current

    val authState =viewModel.registerUser.collectAsState()

    //  ~ Here State is Manage ~
    when{
        authState.value.isLoading ->{
            CircularProgressIndicator()

        }
        authState.value.error != null ->{
            Toast.makeText(context,authState.value.error.toString(),Toast.LENGTH_SHORT).show()

        }
        authState.value.data != null ->{

            Toast.makeText(context,authState.value.data.toString(),Toast.LENGTH_SHORT).show()

        }


    }


    //    Screen Ui

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
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
            label = { Text("FirstName") }

        )

        OutlinedTextField(
            value = lastUseName.value,
            onValueChange = { lastUseName.value = it },
            label = { Text("LastName") }

        )

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

        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {phoneNumber.value = it },
            label = { Text("PHONE NUMBER") }

        )

        Button(onClick = {
            val data = userData(
                firstName = firstUserName.value,
                lastName = lastUseName.value,
                email = email.value,
                password = password.value,
                phoneNumber = phoneNumber.value
            )
            viewModel.registerUser(data)
        }) {
            Text(text = "Register")

        }


    }


}