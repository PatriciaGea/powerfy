package se.tattooink.powerfy.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.ui.components.ProductCard
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun FavoritesRoute(
    onProductClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesScreen(
        isLoading = uiState.isLoading,
        favoriteProducts = uiState.favoriteProducts,
        onProductClick = onProductClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onAddToCartClick = viewModel::addToCart
    )
}

@Composable
private fun FavoritesScreen(
    isLoading: Boolean,
    favoriteProducts: List<Product>,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onAddToCartClick: (Int) -> Unit
) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        favoriteProducts.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                Text(text = "No favorites yet", fontSize = 14.sp, color = PowerfyTextSecondary)
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize().background(Color.White)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Favorites",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(favoriteProducts) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) },
                        onFavoriteClick = { onFavoriteClick(product.id) },
                        onAddToCartClick = { onAddToCartClick(product.id) },
                        isFavorite = true
                    )
                }
            }
        }
    }
}