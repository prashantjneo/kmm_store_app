package com.appstore.auth.product_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appstore.auth.product_list.mapper.toUiModel
import com.appstore.shared.utils.RequestState
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<ProductListViewModel>()

    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Products",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    painter = painterResource(Resources.Icon.Book),
                    contentDescription = "Add Produvt",
                    tint = IconPrimary
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when (val state = uiState.requestState) {

                is RequestState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is RequestState.Success -> {

                    val products = state.data.map { it.toUiModel() }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products) { product ->

                            ProductItem(
                                product = product,
                                onClick = { onProductClick(product.id) },
                                onDeleteClick = { onDeleteClick(product.id) }
                            )
                        }
                    }
                }

                is RequestState.Error -> {
                    Text(
                        state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {}
            }
        }
    }
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
    onDeleteClick: (Int) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Book),
                    contentDescription = "Add Product"
                )
            }
        }
    ) { padding ->

        val viewModel = koinViewModel<ProductListViewModel>()
        val uiState = viewModel.uiState

        Box(modifier = Modifier.padding(padding)) {

            when (val state = uiState.requestState) {

                is RequestState.Loading -> {
                    CircularProgressIndicator()
                }

                is RequestState.Success -> {


                    val products = state.data.map { it.toUiModel() }

                    LazyColumn {
                        items(products) { product ->

                            ProductItem(
                                product = product,
                                onClick = { onProductClick(product.id) },
                                onDeleteClick = { onDeleteClick(product.id) }
                            )
                        }
                    }
                }

                is RequestState.Error -> {
                    Text(state.message)
                }

                else -> {}
            }
        }
    }


}

*/
