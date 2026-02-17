package com.appstore.auth.product_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.appstore.auth.product_list.mapper.toUiModel
import com.appstore.data.domain.model.login.product_list.ProductResponse
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


      LaunchedEffect(Unit) {
          // Optional guard to avoid refetch if already loaded
          if (uiState.requestState is RequestState.Idle ||
              uiState.requestState is RequestState.Error
          ) {
              print(" API Call from update from productList")

              viewModel.getProducts()
          }
      }

    LaunchedEffect(Unit) {
        viewModel.refreshIfNeeded()
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



                   // val products = state.data.map { it.toUiModel() }
                    val products = state.data.map { it.toUiModel().copy() }

                    products.forEach {
                        println("UI product -> ${it.id} ${it.title} ${it.price}")
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = products,
                            key = { it.id }   // ✅ THIS LINE FIXES YOUR ISSUE
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


