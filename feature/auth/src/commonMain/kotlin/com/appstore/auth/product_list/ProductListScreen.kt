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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    onDeleteClick: (Int) -> Unit,   // kept to not break architecture
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<ProductListViewModel>()
    val uiState = viewModel.uiState

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                // Only load first time
                if (uiState.requestState is RequestState.Idle ||
                    uiState.requestState is RequestState.Error
                ) {
                    println("API call initial load")
                    viewModel.getProducts()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                    contentDescription = "Add Product",
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
                        items(
                            items = products,
                            key = { it.id }
                        ) { product ->

                            ProductItem(
                                product = product,
                                onClick = { onProductClick(product.id) },

                                // 🔥 IMPORTANT CHANGE HERE
                                onDeleteClick = {
                                    viewModel.deleteProduct(product.id)
                                }
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
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<ProductListViewModel>()

    val uiState = viewModel.uiState


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                // First load OR refresh after update
                if (uiState.requestState is RequestState.Idle ||
                    uiState.requestState is RequestState.Error
                ) {
                    println("API call initial load")
                    viewModel.getProducts()
                } else {
                    viewModel.getProducts()

                }

            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


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
                    val products = state.data.map { it.toUiModel().copy() }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = products,
                            key = { it.id }
                        ) { product ->
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

*/

