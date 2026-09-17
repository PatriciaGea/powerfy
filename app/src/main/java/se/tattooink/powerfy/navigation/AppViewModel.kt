package se.tattooink.powerfy.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.repository.AuthRepository
import se.tattooink.powerfy.domain.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    authRepository: AuthRepository,
    cartRepository: CartRepository
) : ViewModel() {

    val isLoggedIn: Boolean = authRepository.getCurrentUser()?.let { !it.isAnonymous } ?: false

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.getCartLines().collect { lines ->
                _cartItemCount.update { lines.sumOf { line -> line.quantity } }
            }
        }
    }
}