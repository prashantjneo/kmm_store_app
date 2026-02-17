package com.appstore.navigation

import Screen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.appstore.auth.product_details.ProductDetailScreen
import com.appstore.auth.product_edit.EditProductScreen
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

        composable<Screen.ProductDetail> { backStack ->

            val route = backStack.toRoute<Screen.ProductDetail>()
            val productId = route.productId

            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.navigateUp() },
                onProductEdit = { productId ->
                    navController.navigate(
                        Screen.AddEditProduct(productId)
                    )

                }
            )
        }

        composable<Screen.AddEditProduct> { backStack ->

            val route = backStack.toRoute<Screen.AddEditProduct>()
            val productId = route.product ?: 0
            EditProductScreen(
                productId = productId,
                onBackClick = { navController.navigateUp() },

                onProductUpdated = {
                    navController.popBackStack(
                        Screen.ProductList,
                        false
                    )
                }
            )
        }
    }


}