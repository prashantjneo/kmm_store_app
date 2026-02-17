package com.appstore.auth.product_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appstore.auth.product_list.ProductListViewModel
import com.appstore.shared.utils.RequestState
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onProductUpdated: () -> Unit,
) {

    val viewModel = koinViewModel<ProductListViewModel>()

    val detailState = viewModel.productDetailState
    val updateState = viewModel.updateProductState

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("electronics") }
    var image by remember { mutableStateOf("https://i.pravatar.cc") }

    // -----------------------------
    // LOAD PRODUCT
    // -----------------------------
    LaunchedEffect(productId) {
        viewModel.getProductDetail(productId)
    }

    var isPrefilled by remember { mutableStateOf(false) }

    LaunchedEffect(detailState) {
        if (!isPrefilled && detailState is RequestState.Success) {

            val product = detailState.data

            title = product.title
            price = product.price.toString()
            description = product.description
            category = product.category
            image = product.image
        }
    }

    // -----------------------------
    // UI
    // -----------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Product") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = image,
                onValueChange = { image = it },
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (title.isBlank() || price.isBlank()) return@Button

                    viewModel.updateProduct(
                        productId,
                        title,
                        price,
                        description,
                        category,
                        image
                    )
                }
            ) {
                Text("Update Product")
            }

            Spacer(Modifier.height(16.dp))

            when (updateState) {

                is RequestState.Loading -> {
                    CircularProgressIndicator()
                }

                is RequestState.Success -> {

                    onProductUpdated()
                }

                is RequestState.Error -> {
                    Text(updateState.message, color = Color.Red)
                }

                else -> Unit
            }
        }
    }
}