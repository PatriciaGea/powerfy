package se.tattooink.powerfy.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.domain.repository.ProductRepository
import javax.inject.Inject

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val relatedProducts: List<Product> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val productResult = productRepository.getProductById(productId)
            productResult.fold(
                onSuccess = { product ->
                    _uiState.update { it.copy(isLoading = false, product = product) }
                    loadRelatedProducts(product.category, excludeId = product.id)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Failed to load product")
                    }
                }
            )
        }
    }

    private fun loadRelatedProducts(category: String, excludeId: Int) {
        viewModelScope.launch {
            val result = productRepository.getProductsByCategory(category)
            result.onSuccess { products ->
                val related = products.filter { it.id != excludeId }.take(2)
                _uiState.update { it.copy(relatedProducts = related) }
            }
        }
    }
}