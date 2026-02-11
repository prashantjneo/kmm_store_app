package com.appstore.navigation

import Screen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appstore.auth.product_list.ProductListScreen
import com.appstore.auth.signin.AuthenticationScreen


@Composable
fun SetNavGraph(startDestination: Screen = Screen.Auth) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthenticationScreen(
                navigateToHome = {
                    navController.navigate(Screen.ProductList) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.ProductList> {
            ProductListScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail(productId))
                },
                onAddProductClick = {
                    navController.navigate(Screen.AddEditProduct(null))
                },
                onDeleteClick = { productId ->
                    navController.navigate(Screen.ProductDetail(productId))
                },

                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

    }

}