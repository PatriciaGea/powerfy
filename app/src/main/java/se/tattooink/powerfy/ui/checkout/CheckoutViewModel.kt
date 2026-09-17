package se.tattooink.powerfy.ui.checkout

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

enum class DeliveryMethod(val label: String, val subtitle: String, val fee: Double) {
    STANDARD("Standard Delivery", "3-5 business days", 4.99),
    EXPRESS("Express Delivery", "1-2 business days", 12.99)
}

data class CheckoutUiState(
    val isLoading: Boolean = true,
    val customerName: String = "",
    val subtotal: Double = 0.0,
    val selectedMethod: DeliveryMethod = DeliveryMethod.STANDARD
) {
    val total: Double get() = subtotal + selectedMethod.fee
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    authRepository: se.tattooink.powerfy.domain.repository.AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CheckoutUiState(customerName = authRepository.getCurrentUser()?.name ?: "")
    )
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()
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
                val subtotal = cartLines.sumOf { line ->
                    val product = allProducts.find { it.id == line.productId }
                    (product?.price ?: 0.0) * line.quantity
                }
                _uiState.update { it.copy(isLoading = false, subtotal = subtotal) }
            }
        }
    }

    fun selectDeliveryMethod(method: DeliveryMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }
}