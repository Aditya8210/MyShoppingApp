package com.wp7367.myshoppingapp.ui_layer.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.ui_layer.screens.others.CheckOutScreenUi
import com.wp7367.myshoppingapp.ui_layer.screens.others.EachProductDetailScreen
import com.wp7367.myshoppingapp.ui_layer.screens.others.EditAddressScreen
import com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage.HomeScreenUi
import com.wp7367.myshoppingapp.ui_layer.screens.auth.LoginScreen
import com.wp7367.myshoppingapp.ui_layer.screens.others.OrderSuccess
import com.wp7367.myshoppingapp.ui_layer.screens.others.SeeAllProductUi
import com.wp7367.myshoppingapp.ui_layer.screens.auth.SignUpScreen
import com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage.CartPage
import com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage.FavoritePage
import com.wp7367.myshoppingapp.ui_layer.screens.homeScreenPage.ProfilePage
import com.wp7367.myshoppingapp.ui_layer.screens.others.AddressScreen
import com.wp7367.myshoppingapp.ui_layer.screens.others.NotificationScreen
import com.wp7367.myshoppingapp.ui_layer.screens.others.OrderScreen
import com.wp7367.myshoppingapp.ui_layer.screens.others.SeeAllCategoryScreen

@Composable
fun AppNav(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    val shouldShowBottomBar = remember(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route ?: ""
        !currentRoute.contains("LogInScreen") && !currentRoute.contains("SignUpScreen")
    }

    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LogInSignUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0.dp),
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(64.dp)
                        .border(
                            width = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { 
                            it.hasRoute(item.route::class) 
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { 
                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(22.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                bottom = if (shouldShowBottomBar) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            NavHost(navController = navController, startDestination = startScreen) {
                navigation<SubNavigation.LogInSignUpScreen>(startDestination = Routes.LogInScreen) {
                    composable<Routes.LogInScreen> {
                        LoginScreen(navController = navController)
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController = navController)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        HomeScreenUi(navController = navController)
                    }
                    composable<Routes.FavoriteScreen> {
                        FavoritePage(navController = navController)
                    }
                    composable<Routes.CartScreen> {
                        CartPage(navController = navController)
                    }
                    composable<Routes.ProfileScreen> {
                        ProfilePage(navController = navController, firebaseAuth = firebaseAuth)
                    }
                }

                composable<Routes.EachProductDetailScreen> {
                    val data = it.toRoute<Routes.EachProductDetailScreen>()
                    EachProductDetailScreen(navController = navController, productId = data.productId)
                }

                composable<Routes.CheckOutScreen> {
                    val data = it.toRoute<Routes.CheckOutScreen>()
                    CheckOutScreenUi(
                        navController = navController,
                        productId = data.productId,
                        quantity = data.quantity,
                        size = data.size,
                        color = data.color
                    )
                }

                composable<Routes.EditAddressScreen> {
                    val data = it.toRoute<Routes.EditAddressScreen>()
                    EditAddressScreen(navController = navController, addressId = data.addressId)
                }

                composable<Routes.SeeAllProduct> {
                    SeeAllProductUi(navController = navController)
                }

                composable<Routes.SeeAllCategories> {
                    SeeAllCategoryScreen(navController = navController)
                }

                composable<Routes.OrderHistoryScreen> {
                    OrderScreen(navController = navController, orderViewmodel = hiltViewModel())
                }

                composable<Routes.AddressScreen> {
                    AddressScreen(navController = navController)
                }

                composable<Routes.OrderSuccess> {
                    OrderSuccess(navController = navController)
                }

                composable<Routes.NotificationScreen> {
                    NotificationScreen(navController = navController)
                }
            }
        }
    }
}

sealed class BottomNavItem(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(Routes.HomeScreen, "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Favorite : BottomNavItem(Routes.FavoriteScreen, "Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Cart : BottomNavItem(Routes.CartScreen, "Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    object Profile : BottomNavItem(Routes.ProfileScreen, "Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
}
