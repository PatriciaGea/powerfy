package se.tattooink.powerfy.ui.home

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

data class HomeUiState(
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val products: List<Product> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val cartProductIds: Set<Int> = emptySet(),
    val isLoadingProducts: Boolean = true,
    val productsErrorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(run {
        val user = authRepository.getCurrentUser()
        if (user != null && !user.isAnonymous) {
            HomeUiState(isLoggedIn = true, userName = user.name, userEmail = user.email)
        } else {
            HomeUiState(isLoggedIn = false)
        }
    })
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
        observeFavorites()
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.getCartLines().collect { lines ->
                _uiState.update { it.copy(cartProductIds = lines.map { line -> line.productId }.toSet()) }
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingProducts = true, productsErrorMessage = null) }
            val result = productRepository.getElectronicsProducts()
            result.fold(
                onSuccess = { products ->
                    _uiState.update { it.copy(isLoadingProducts = false, products = products) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoadingProducts = false, productsErrorMessage = error.message ?: "Failed to load products")
                    }
                }
            )
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteIds().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids.toSet()) }
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