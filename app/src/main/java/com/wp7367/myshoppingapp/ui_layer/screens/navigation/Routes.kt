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

    @Serializable
    data class CheckOutScreen(
        val productId: String? = null,
        val quantity: Int = 1,
        val size: String = "",
        val color: String = ""
    )


    @Serializable
    data class EditAddressScreen(val addressId: String? = null)

    @Serializable
    object SeeAllProduct

    @Serializable
    object SeeAllCategories

    @Serializable
    object OrderHistoryScreen

    @Serializable
    object AddressScreen

    @Serializable
    object OrderSuccess

    @Serializable
    object NotificationScreen


}
