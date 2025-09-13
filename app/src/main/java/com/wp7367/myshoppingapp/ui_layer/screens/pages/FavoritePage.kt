package com.wp7367.myshoppingapp.ui_layer.screens.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.ui_layer.screens.MyViewModel

@Composable
fun FavoritePage(modifier: Modifier = Modifier) {

//    val favoriteSt = viewModel.getProductById.collectAsState()
//
//
//
//
//    LaunchedEffect(key1 = Unit) {
//        viewModel.getProductById(productId)
//
//    }
//
//
//    when{
//        favoriteSt.value.isLoading ->{
//            CircularProgressIndicator()
//        }
//        favoriteSt.value.error != null ->{
//            Text(text = favoriteSt.value.error!!)
//        }
//        favoriteSt.value.data != null ->{
//            Text(text = favoriteSt.value.data!!.name)
//
//
//            LazyColumn (modifier = Modifier.fillMaxSize()){
//
//
//
//            }

        Text(text = "Favorite Page")




        }

