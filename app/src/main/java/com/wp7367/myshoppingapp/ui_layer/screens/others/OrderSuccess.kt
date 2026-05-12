package com.wp7367.myshoppingapp.ui_layer.screens.others

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes

@Composable
fun OrderSuccess(
    modifier: Modifier = Modifier,
    navController: NavController
) {




    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


           Image(painter = painterResource(id = com.wp7367.myshoppingapp.R.drawable.orderconform)
               , contentDescription = "success")

            Spacer(modifier = modifier.height(20.dp))

            Spacer(modifier = modifier.height(20.dp))

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    navController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.HomeScreen) {
                            inclusive = true
                        }
                    }
                }) {
                Text(text = "Back to Home")
            }
        }
    }

}


