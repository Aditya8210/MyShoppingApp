package com.wp7367.myshoppingapp.ui_layer.screens.navigation

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.ui_layer.screens.HomeScreenUi
import com.wp7367.myshoppingapp.ui_layer.screens.LoginScreen
import com.wp7367.myshoppingapp.ui_layer.screens.SignUpScreen
import com.wp7367.myshoppingapp.ui_layer.screens.pages.CartPage
import com.wp7367.myshoppingapp.ui_layer.screens.pages.FavoritePage
import com.wp7367.myshoppingapp.ui_layer.screens.pages.ProfilePage


@Composable
fun AppNav(firebaseAuth: FirebaseAuth,){

    val navController = rememberNavController()



//---------------------Step 2 ~ BottomNavBar ~ ------------------------------------------------------------------------

    // Get the current back stack entry reactively
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Get the current route from the back stack entry
    val currentRoute = navBackStackEntry?.destination?.route

    val navItemList = listOf(
        navItemList("Home",Icons.Default.Home),
        navItemList("Favorite",Icons.Default.Favorite),
        navItemList("Cart",Icons.Default.ShoppingCart),
        navItemList("Profile",Icons.Default.AccountBox)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    // Derive shouldShowBottomBar directly from the currentRoute
    // This will automatically update when currentRoute changes
    val shouldShowBottomBar by remember(currentRoute) {
        derivedStateOf {
            when (currentRoute) {
                Routes.LogInScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
                else -> true
            }
        }
    }
//--------------------------------------------------------------------------------------------------



    var StartScreen = if (firebaseAuth.currentUser == null){
        SubNavigation.LogInSignUpScreen
    }
    else{
        SubNavigation.MainHomeScreen
    }


//-----------------------Step 3 ~ BottomNavBar ~ -----------------------------------------------
    Scaffold (
        bottomBar = {
            // Use shouldShowBottomBar directly (no .value needed because of 'by' delegate)
            if (shouldShowBottomBar) {
            NavigationBar {
                navItemList.forEachIndexed { index, navItemList ->

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index

                            when (selectedIndex) {
                                0 -> navController.navigate(Routes.HomeScreen)
                                1 -> navController.navigate(Routes.FavoriteScreen)
                                2 -> navController.navigate(Routes.CartScreen)
                                3 -> navController.navigate(Routes.ProfileScreen)
                            }
                        },
                        label = { navItemList.label },
                        icon = {
                            Icon(navItemList.icon, contentDescription = navItemList.label)
                        }
                    )
                }
            }
        }
        }

        //---------------------Step 4 ~ BottomNavBar ~ ---------------------------------------------
    ){

        innerPadding ->
        Box(modifier = Modifier.padding(
                // Use shouldShowBottomBar directly
                bottom = if (shouldShowBottomBar) innerPadding.calculateBottomPadding() else 0.dp
            )
        )
        {
            NavHost(navController = navController, startDestination = StartScreen)
            {

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
                    composable <Routes.FavoriteScreen>{
                        FavoritePage()
                    }
                    composable <Routes.CartScreen> {
                        CartPage()
                    }
                    composable <Routes.ProfileScreen>{
                        ProfilePage()
                    }
                }
            }
        }
    }
}



//------------------ Step 1 ~ Data class for BottomNavBar ~ ----------------------------------

data class navItemList(
    val label: String,
    val icon: ImageVector,
)
