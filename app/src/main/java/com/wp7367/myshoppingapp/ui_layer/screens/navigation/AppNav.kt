package com.wp7367.myshoppingapp.ui_layer.screens.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.ui_layer.screens.HomeScreenUi
import com.wp7367.myshoppingapp.ui_layer.screens.LoginScreen
import com.wp7367.myshoppingapp.ui_layer.screens.SignUpScreen

@Composable

//         Agar user AllReady Login Hai tb HomeScreen show karo warna Login Screen show karo uska liya
//         modifier ma FireBase ka Auth le lenga bcz usma current user ka Access Hai
fun AppNav(firebaseAuth: FirebaseAuth,){


//---------------------  Step 2 ~ To Create BottomNavigation Bar ~ ---------------------------------

    val navItemList = listOf(
        navItemList("Home",Icons.Default.Home),
        navItemList("Favorite",Icons.Default.Favorite),
        navItemList("Cart",Icons.Default.ShoppingCart),
        navItemList("Profile",Icons.Default.AccountBox)
    )

    var selectedIndex by remember { mutableStateOf(0) }

//--------------------------------------------------------------------------------------------------

    val navController = rememberNavController()

    var StartScreen = if (firebaseAuth.currentUser == null){

        SubNavigation.LogInSignUpScreen
    }
    else{

        SubNavigation.MainHomeScreen
    }



    //--------------------------------------------------------------------------------------------------
    //             Step 3  ~ To Create BottomNavigation Bar ~
    Scaffold (
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index,navItemList ->

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {selectedIndex = index},
                        label = {navItemList.label},
                        icon = {
                            Icon(navItemList.icon, contentDescription = navItemList.label)
                        }
                    )

                }
            }
        }
    ){
        ContentScreen(modifier = Modifier.padding(it),selectedIndex,navController = navController)
    }

//--------------------------------------------------------------------------------------------------


    NavHost(navController = navController, startDestination = StartScreen) {


        navigation<SubNavigation.LogInSignUpScreen>(startDestination = Routes.LogInScreen){
            composable<Routes.LogInScreen>{
                LoginScreen(navController= navController)
            }

            composable<Routes.SignUpScreen>{
                SignUpScreen(navController= navController)

            }
        }


        navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){

            composable<Routes.HomeScreen>{
                HomeScreenUi(navController=navController)
            }
        }


    }



}

//------------------------  Step 3/4 ~ To Create BottomNavigation Bar ~ --------------------------------------
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavHostController, ) {

    when(selectedIndex){
        0 -> HomeScreenUi(navController = navController)
        1 -> {}
        2 -> {}
        3 -> {}
    }

}

//--------------------------------------------------------------------------------------------------

//       Step 1 ~ Data Class for BottomNavigation Bar ~

data class navItemList(
    val label: String,
    val icon: ImageVector,
)


