package se.tattooink.powerfy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.ui.components.TopBar
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun HomeRoute(
    onNavigateToIntro: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    HomeScreen(
        isLoggedIn = uiState.isLoggedIn,
        userName = uiState.userName,
        userEmail = uiState.userEmail,
        onProfileClick = {
            if (uiState.isLoggedIn) {
                onNavigateToProfile()
            } else {
                onNavigateToIntro()
            }
        }
    )
}

@Composable
private fun HomeScreen(
    isLoggedIn: Boolean,
    userName: String,
    userEmail: String,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            isLoggedIn = isLoggedIn,
            onFavoriteClick = {},
            onCartClick = {},
            onProfileClick = onProfileClick
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isLoggedIn) {
                Text(text = "Logged in as: $userName", color = Color.Black)
                Text(text = userEmail, color = PowerfyTextSecondary)
            } else {
                Text(text = "Not logged in (Guest)", color = PowerfyTextSecondary)
            }
        }
    }
}