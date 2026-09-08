package se.tattooink.powerfy.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.repository.AuthRepository
import javax.inject.Inject

data class IntroUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val guestLoginSucceeded: Boolean = false
)

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IntroUiState())
    val uiState: StateFlow<IntroUiState> = _uiState.asStateFlow()

    fun loginAsGuest() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.signInAnonymously()
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, guestLoginSucceeded = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Guest login failed") }
                }
            )
        }
    }
}