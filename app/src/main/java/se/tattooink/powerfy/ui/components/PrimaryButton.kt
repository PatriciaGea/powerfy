package se.tattooink.powerfy.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfyShapes

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = PowerfyShapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = PowerfyPrimary,
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}