package com.wp7367.myshoppingapp.ui_layer.screens.navigation

import kotlinx.serialization.Serializable


sealed class SubNavigation {
    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LogInSignUpScreen : SubNavigation()
}



sealed class Routes {

     @Serializable
     object HomeScreen

    @Serializable
    object FavoriteScreen

    @Serializable
    object CartScreen

    @Serializable
    object ProfileScreen


    @Serializable
    object LogInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    data class EachProductDetailScreen(val productId: String)

}


