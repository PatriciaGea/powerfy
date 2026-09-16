package se.tattooink.powerfy.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.ui.components.BackButton
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

private val LogoutRed = Color(0xFFFF5722)
private val LogoutRedBorder = Color(0xFFF44336)

@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) {
            onLoggedOut()
        }
    }

    ProfileScreen(
        userName = uiState.userName,
        userEmail = uiState.userEmail,
        onBackClick = onBackClick,
        onLogoutClick = viewModel::logout
    )
}

@Composable
private fun ProfileScreen(
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        BackButton(onClick = onBackClick)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PowerfyPrimary)
            )
            Text(
                text = userName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = userEmail,
                fontSize = 12.sp,
                color = PowerfyTextSecondary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, LogoutRedBorder, RoundedCornerShape(12.dp))
                .clickable(onClick = onLogoutClick)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Log Out",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LogoutRed
            )
        }
    }
}