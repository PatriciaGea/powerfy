package se.tattooink.powerfy.ui.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfySurface
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun ConfirmationRoute(onBackToHomeClick: () -> Unit) {
    val orderNumber = remember { "PWRFY-${(10000..99999).random()}" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(PowerfyPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✓", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(24.dp))

        Text(
            text = "Order Confirmed!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(PowerfySurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Order #: $orderNumber", fontSize = 13.sp, color = PowerfyTextSecondary)
            Text(text = "Your payment was processed successfully.", fontSize = 13.sp, color = PowerfyTextSecondary)
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(24.dp))

        PrimaryButton(text = "Back to Home", onClick = onBackToHomeClick)
    }
}