package com.wp7367.myshoppingapp.ui_layer.screens.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.ui.theme.Pink80
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes


@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController, firebaseAuth: FirebaseAuth) {

    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        OutlinedButton(onClick = {

            firebaseAuth.signOut()
            navController.navigate(Routes.LogInScreen)
        },

            modifier = Modifier.size(width = 200.dp, height = 50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(),



            ) {
            Text(text = "LogOut")
        }
    }

}