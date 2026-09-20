package se.tattooink.powerfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfySurface
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun CategoryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
        color = if (isSelected) Color.White else PowerfyTextSecondary,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) PowerfyPrimary else PowerfySurface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
