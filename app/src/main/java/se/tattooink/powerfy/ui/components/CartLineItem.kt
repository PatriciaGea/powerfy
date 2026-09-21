package se.tattooink.powerfy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.ui.theme.PowerfyBorder
import se.tattooink.powerfy.util.toSekPrice

@Composable
fun CartLineItem(
    product: Product,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.title,
                fontSize = 13.sp,
                color = Color.Black
            )
            Text(
                text = product.price.toSekPrice(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, PowerfyBorder, RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepButton(symbol = "−", onClick = onDecrement)
            Divider()
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .height(28.dp)
                    .width(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$quantity", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Divider()
            StepButton(symbol = "+", onClick = onIncrement)
        }
    }
}

@Composable
private fun StepButton(symbol: String, onClick: () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(28.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = symbol, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}

@Composable
private fun Divider() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .width(1.dp)
            .height(28.dp)
            .background(PowerfyBorder)
    )
}