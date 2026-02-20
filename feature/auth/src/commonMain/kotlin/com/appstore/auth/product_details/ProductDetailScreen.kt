package com.appstore.auth.product_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.appstore.shared.utils.RequestState
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.RobotoCondensedFont
import com.nutrisport.shared.Surface
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onProductEdit: (Int) -> Unit,
) {
    val viewModel = koinViewModel<ProductDetailViewModel>()

    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.getProductDetail(productId)
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Products Detail",
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

                    val product = state.data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = product.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            )
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-24).dp),
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {

                                Text(
                                    text = product.title,
                                    fontSize = FontSize.LARGE,
                                    color = TextPrimary,
                                    fontFamily = RobotoCondensedFont(),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    text = "₹ ${product.price}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(18.dp))

                                Text(
                                    text = "Product Description",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = product.description,
                                    fontSize = FontSize.MEDIUM,
                                    color = TextPrimary,
                                    fontFamily = RobotoCondensedFont(),
                                    lineHeight = 22.sp
                                )

                                Spacer(Modifier.height(32.dp))

                                Button(
                                    onClick = { onProductEdit(product.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        "Edit Product",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }

                is RequestState.Error -> {
                    Text("Something went wrong")
                }

                else -> {}
            }
        }
    }
}