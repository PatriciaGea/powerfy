package se.tattooink.powerfy.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.R
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun IntroRoute(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGuestLoginSuccess: () -> Unit,
    viewModel: IntroViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.guestLoginSucceeded) {
        onGuestLoginSuccess()
    }

    IntroScreen(
        onLoginClick = onLoginClick,
        onSignUpClick = onSignUpClick,
        onGuestClick = viewModel::loginAsGuest
    )
}

@Composable
private fun IntroScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    val overlayGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to Color.White.copy(alpha = 0.12f),
            0.65385f to Color.Transparent
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .background(overlayGradient)
            .padding(horizontal = 34.dp)
            .padding(bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_intro_illustration),
            contentDescription = null,
            modifier = Modifier
                .width(300.dp)
                .height(426.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = "Your first destination for electronics shopping",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            text = "Log In",
            onClick = onLoginClick
        )

        Text(
            text = "Don't have an account? Sign Up!",
            fontSize = 13.sp,
            color = PowerfyTextSecondary,
            modifier = Modifier.clickable(onClick = onSignUpClick)
        )

        Text(
            text = "Continue as Guest",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = PowerfyPrimary,
            modifier = Modifier.clickable(onClick = onGuestClick)
        )
    }
}