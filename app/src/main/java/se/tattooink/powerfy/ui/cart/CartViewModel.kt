package se.tattooink.powerfy.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.domain.repository.CartRepository
import se.tattooink.powerfy.domain.repository.ProductRepository
import javax.inject.Inject

data class CartLineUiModel(
    val product: Product,
    val quantity: Int
)

data class CartUiState(
    val isLoading: Boolean = true,
    val lines: List<CartLineUiModel> = emptyList(),
    val total: Double = 0.0
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            val result = productRepository.getElectronicsProducts()
            result.onSuccess { products -> allProducts = products }
            observeCart()
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.getCartLines().collect { cartLines ->
                val lines = cartLines.mapNotNull { line ->
                    val product = allProducts.find { it.id == line.productId }
                    product?.let { CartLineUiModel(product = it, quantity = line.quantity) }
                }
                val total = lines.sumOf { it.product.price * it.quantity }
                _uiState.update { it.copy(isLoading = false, lines = lines, total = total) }
            }
        }
    }

    fun increment(productId: Int) {
        viewModelScope.launch {
            cartRepository.incrementQuantity(productId)
        }
    }

    fun decrement(productId: Int) {
        viewModelScope.launch {
            cartRepository.decrementQuantity(productId)
        }
    }
}