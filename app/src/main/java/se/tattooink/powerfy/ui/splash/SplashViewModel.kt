package se.tattooink.powerfy.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashNavigationEvent {
    data object ToIntro : SplashNavigationEvent
    data object ToHome : SplashNavigationEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navigationEvent: StateFlow<SplashNavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS)
            val isUserLoggedIn = checkUserSession()
            _navigationEvent.value = if (isUserLoggedIn) {
                SplashNavigationEvent.ToHome
            } else {
                SplashNavigationEvent.ToIntro
            }
        }
    }

    private fun checkUserSession(): Boolean {
        return false
    }

    companion object {
        private const val SPLASH_DELAY_MS = 1500L
    }
}
