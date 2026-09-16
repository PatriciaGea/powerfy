package se.tattooink.powerfy.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.tattooink.powerfy.domain.repository.AuthRepository
import javax.inject.Inject

sealed interface SplashNavigationEvent {
    data object ToIntro : SplashNavigationEvent
    data object ToHome : SplashNavigationEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navigationEvent: StateFlow<SplashNavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS)
            val hasSession = authRepository.getCurrentUser() != null
            _navigationEvent.value = if (hasSession) {
                SplashNavigationEvent.ToHome
            } else {
                SplashNavigationEvent.ToIntro
            }
        }
    }

    companion object {
        private const val SPLASH_DELAY_MS = 4500L
    }
}