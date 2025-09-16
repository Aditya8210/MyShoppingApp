package com.wp7367.myshoppingapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.domain_layer.models.userData

@Composable


fun SignUpScreen(viewModel: MyViewModel = hiltViewModel(),navController: NavController){

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
                .size(235.dp)
                .offset(x = 180.dp, y = (-60).dp)
        ) {

            Image(
                painter = painterResource(id = com.wp7367.myshoppingapp.R.drawable.circle332212),
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
            singleLine = true

        )
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = lastUseName.value,
            onValueChange = { lastUseName.value = it },
            label = { Text("LastName") },
            singleLine = true

        )

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Gmail") },
            singleLine = true

        )
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("PASSWORD") },
            singleLine = true

        )
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {phoneNumber.value = it },
            label = { Text("PHONE NUMBER") },
            singleLine = true

        )
        Spacer(modifier = Modifier.height(10.dp))

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
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(215.dp)
                .offset(x = 0.dp, y = (+29).dp)
            ){


                Image(painter = painterResource(id = com.wp7367.myshoppingapp.R.drawable.circle33221),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())
            }

        }


    }

}


}