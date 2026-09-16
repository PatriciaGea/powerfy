package se.tattooink.powerfy.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.ui.components.ProductCard
import se.tattooink.powerfy.ui.components.TopBar
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun FavoritesRoute(
    onProductClick: (Int) -> Unit,
    onFavoriteIconClick: () -> Unit,
    onCartClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToIntro: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesScreen(
        isLoggedIn = uiState.isLoggedIn,
        isLoading = uiState.isLoading,
        favoriteProducts = uiState.favoriteProducts,
        onProductClick = onProductClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onFavoriteIconClick = onFavoriteIconClick,
        onCartClick = onCartClick,
        onProfileClick = {
            if (uiState.isLoggedIn) {
                onNavigateToProfile()
            } else {
                onNavigateToIntro()
            }
        }
    )
}

@Composable
private fun FavoritesScreen(
    isLoggedIn: Boolean,
    isLoading: Boolean,
    favoriteProducts: List<Product>,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onFavoriteIconClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        TopBar(
            isLoggedIn = isLoggedIn,
            onFavoriteClick = onFavoriteIconClick,
            onCartClick = onCartClick,
            onProfileClick = onProfileClick
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            favoriteProducts.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No favorites yet",
                        fontSize = 14.sp,
                        color = PowerfyTextSecondary
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        Text(
                            text = "Favorites",
                            fontSize = 16.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    items(favoriteProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) },
                            onFavoriteClick = { onFavoriteClick(product.id) },
                            onAddToCartClick = {},
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }
}