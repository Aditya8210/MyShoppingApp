package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun EachProductDetailScreen(productId: String ,viewModels: MyViewModel = hiltViewModel(), navController: NavController) {

    val EachProductDetailSt = viewModels.getProductById.collectAsState()


    LaunchedEffect(key1 = Unit){
       viewModels.getProductById(productId)

    }

    when{
        EachProductDetailSt.value.isLoading ->{
            CircularProgressIndicator()
        }
        EachProductDetailSt.value.error!= null ->{
            Text(text = EachProductDetailSt.value.error!!)

        }
        EachProductDetailSt.value.data != null ->{


            Column (modifier = Modifier.fillMaxSize())
            {

                Text(text = EachProductDetailSt.value.data!!.name)
            }

        }
    }

}