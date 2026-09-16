package se.tattooink.powerfy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import se.tattooink.powerfy.ui.components.SearchBar
import se.tattooink.powerfy.ui.components.TopBar

@Composable
fun HomeRoute(
    onNavigateToIntro: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onProductClick: (Int) -> Unit,
    onFavoriteIconClick: () -> Unit,
    onCartIconClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        isLoggedIn = uiState.isLoggedIn,
        products = uiState.products,
        favoriteIds = uiState.favoriteIds,
        isLoadingProducts = uiState.isLoadingProducts,
        productsErrorMessage = uiState.productsErrorMessage,
        onProductClick = onProductClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onAddToCartClick = viewModel::addToCart,
        onFavoriteIconClick = onFavoriteIconClick,
        onCartIconClick = onCartIconClick,
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
private fun HomeScreen(
    isLoggedIn: Boolean,
    products: List<Product>,
    favoriteIds: Set<Int>,
    isLoadingProducts: Boolean,
    productsErrorMessage: String?,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onAddToCartClick: (Int) -> Unit,
    onFavoriteIconClick: () -> Unit,
    onCartIconClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            isLoggedIn = isLoggedIn,
            onFavoriteClick = onFavoriteIconClick,
            onCartClick = onCartIconClick,
            onProfileClick = onProfileClick
        )

        when {
            isLoadingProducts -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            productsErrorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = productsErrorMessage,
                        color = Color.Red,
                        fontSize = 13.sp
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
                    item(span = { GridItemSpan(2) }) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            SearchBar()
                            Text(
                                text = "Top Deals on Electronics",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) },
                            onFavoriteClick = { onFavoriteClick(product.id) },
                            onAddToCartClick = { onAddToCartClick(product.id) },
                            isFavorite = product.id in favoriteIds
                        )
                    }
                }
            }
        }
    }
}