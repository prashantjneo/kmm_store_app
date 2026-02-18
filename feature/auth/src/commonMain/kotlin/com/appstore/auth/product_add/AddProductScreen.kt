package com.appstore.auth.product_add


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    viewModel: ProductListViewModel,

    onBackClick: () -> Unit,
    onProductAdded: () -> Unit
) {

    //val viewModel = koinViewModel<ProductListViewModel>()
    val addState = viewModel.addState

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("https://i.pravatar.cc") }

    Scaffold(
        containerColor = Surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add Product",
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                title,
                { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                price,
                { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                description,
                { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                category,
                { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                image,
                { image = it },
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (title.isBlank() || price.isBlank()) return@Button

                    viewModel.addProduct(
                        title,
                        price,
                        description,
                        category,
                        image
                    )
                }
            ) {
                Text("Add Product")
            }

            Spacer(Modifier.height(16.dp))

            when (addState) {

                is RequestState.Loading -> {
                    CircularProgressIndicator()
                }

                is RequestState.Success -> {
                    LaunchedEffect(addState) {

                        // give Compose one frame to recompose list
                        kotlinx.coroutines.delay(150)

                        onProductAdded()
                    }
                }

                is RequestState.Error -> {
                    Text(addState.message, color = Color.Red)
                }

                else -> Unit
            }
        }
    }
}
