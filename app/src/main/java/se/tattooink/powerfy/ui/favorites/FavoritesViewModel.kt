package se.tattooink.powerfy.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.domain.repository.AuthRepository
import se.tattooink.powerfy.domain.repository.CartRepository
import se.tattooink.powerfy.domain.repository.FavoriteRepository
import se.tattooink.powerfy.domain.repository.ProductRepository
import javax.inject.Inject

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val favoriteProducts: List<Product> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(run {
        val user = authRepository.getCurrentUser()
        FavoritesUiState(isLoggedIn = user != null && !user.isAnonymous)
    })
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            val result = productRepository.getElectronicsProducts()
            result.onSuccess { products -> allProducts = products }
            observeFavorites()
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteIds().collect { ids ->
                val favoriteProducts = allProducts.filter { it.id in ids }
                _uiState.update { it.copy(isLoading = false, favoriteProducts = favoriteProducts) }
            }
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(productId)
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(productId)
        }
    }
}