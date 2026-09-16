package se.tattooink.powerfy.ui.profile

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

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val loggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(run {
        val user = authRepository.getCurrentUser()
        ProfileUiState(userName = user?.name ?: "", userEmail = user?.email ?: "")
    })
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(loggedOut = true) }
        }
    }
}