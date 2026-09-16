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
import se.tattooink.powerfy.domain.repository.ProductRepository
import javax.inject.Inject

data class HomeUiState(
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val products: List<Product> = emptyList(),
    val isLoadingProducts: Boolean = true,
    val productsErrorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
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
}