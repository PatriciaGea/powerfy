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
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val favoriteIds: Set<Int> = emptySet(),
    val cartProductIds: Set<Int> = emptySet(),
    val isLoadingProducts: Boolean = true,
    val productsErrorMessage: String? = null
) {
    val visibleProducts: List<Product>
        get() = when (selectedCategory) {
            null -> products.sortedBy { if (it.category == "laptops") 0 else 1 }
            else -> products.filter { it.category == selectedCategory }
        }
}

private val CATEGORY_DISPLAY_ORDER = listOf("smartphones", "laptops", "tablets", "mobile-accessories")

fun categoryLabel(category: String): String = when (category) {
    "mobile-accessories" -> "Accessories"
    else -> category.replaceFirstChar { it.uppercase() }
}

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
                    val categories = CATEGORY_DISPLAY_ORDER.filter { category ->
                        products.any { it.category == category }
                    }
                    _uiState.update {
                        it.copy(isLoadingProducts = false, products = products, categories = categories)
                    }
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

    fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}