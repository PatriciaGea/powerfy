package se.tattooink.powerfy.ui.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.ui.components.BackButton
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.components.ProductCard
import se.tattooink.powerfy.ui.theme.PowerfyStarYellow
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun ProductDetailRoute(
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ProductDetailScreen(
        isLoading = uiState.isLoading,
        product = uiState.product,
        relatedProducts = uiState.relatedProducts,
        errorMessage = uiState.errorMessage,
        onBackClick = onBackClick,
        onProductClick = onProductClick,
        onAddToCartClick = viewModel::addToCart
    )
}

@Composable
private fun ProductDetailScreen(
    isLoading: Boolean,
    product: Product?,
    relatedProducts: List<Product>,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    onAddToCartClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }

            product != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BackButton(onClick = onBackClick)

                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Text(
                        text = product.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = product.description,
                        fontSize = 12.sp,
                        color = PowerfyTextSecondary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "★", fontSize = 12.sp, color = PowerfyStarYellow)
                        Text(
                            text = " ${product.rating}",
                            fontSize = 12.sp,
                            color = PowerfyTextSecondary
                        )
                    }

                    Text(
                        text = "$${"%.2f".format(product.price)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    PrimaryButton(text = "ADD TO CART", onClick = onAddToCartClick)

                    if (relatedProducts.isNotEmpty()) {
                        Text(
                            text = "Frequently Viewed",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            relatedProducts.forEach { related ->
                                ProductCard(
                                    product = related,
                                    onClick = { onProductClick(related.id) },
                                    onFavoriteClick = {},
                                    onAddToCartClick = { onProductClick(related.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}