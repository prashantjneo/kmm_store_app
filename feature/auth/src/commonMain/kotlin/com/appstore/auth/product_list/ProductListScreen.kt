package com.appstore.auth.product_list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appstore.auth.product_list.mapper.toUiModel
import com.appstore.shared.utils.RequestState
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,

    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        if (viewModel.uiState.requestState is RequestState.Idle) {
            viewModel.getProducts()
        }
    }
    Scaffold(
        containerColor = Surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Products",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
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
                },

                )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                containerColor = Color(0xFF6750A4),   // Material purple
                contentColor = Color.White,
                modifier = Modifier
                    .shadow(12.dp, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Add),
                    contentDescription = "Add Product",
                    modifier = Modifier.size(28.dp)
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

                    println("----- RAW LIST FROM UI STATE -----")

                    state.data.forEach {
                        println("RAW -> id=${it.id} title=${it.title} price=${it.price}")
                    }

                    val products = state.data.map { it.toUiModel() }

                    println("----- MAPPED UI LIST -----")

                    products.forEach {
                        println("UI -> id=${it.id} title=${it.title} price=${it.price}")
                    }

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

