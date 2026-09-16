package se.tattooink.powerfy.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import se.tattooink.powerfy.domain.repository.AuthRepository
import javax.inject.Inject

data class HomeUiState(
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val userEmail: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState: HomeUiState = run {
        val user = authRepository.getCurrentUser()
        if (user != null && !user.isAnonymous) {
            HomeUiState(isLoggedIn = true, userName = user.name, userEmail = user.email)
        } else {
            HomeUiState(isLoggedIn = false)
        }
    }
}