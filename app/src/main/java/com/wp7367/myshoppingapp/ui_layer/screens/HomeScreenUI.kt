package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreenUi(viewModels: MyViewModel = hiltViewModel(),navController: NavController){




    //  -- Here State is Collect --

    val ctState = viewModels.getAllCategory.collectAsState()
    val productState = viewModels.getAllProduct.collectAsState()



    LaunchedEffect(key1 = Unit){
        viewModels.getAllCategory()
        viewModels.getAllProduct()
    }

// ------------------- ~ New UI ~ ------------------------------------------------------------------








// ----------------   ~ Old UI ~ -------------------------------------------------------------------

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow {
            //  here check proper items  datatype like List
            items(ctState.value.data){

                Text(text = it!!.name)
                Text(text = it.imageUri)

            }
        }
    }


}