package se.tattooink.powerfy.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    cartRepository: CartRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(computeIsLoggedIn())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener {
            _isLoggedIn.value = computeIsLoggedIn()
        }
        viewModelScope.launch {
            cartRepository.getCartLines().collect { lines ->
                _cartItemCount.update { lines.sumOf { line -> line.quantity } }
            }
        }
    }

    private fun computeIsLoggedIn(): Boolean {
        val user = firebaseAuth.currentUser
        return user != null && !user.isAnonymous
    }
}