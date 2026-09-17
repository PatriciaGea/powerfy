package se.tattooink.powerfy.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.tattooink.powerfy.ui.components.CartLineItem
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.theme.PowerfyBorder
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun CartRoute(
    onCheckoutClick: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CartScreen(
        isLoading = uiState.isLoading,
        lines = uiState.lines,
        total = uiState.total,
        onIncrement = viewModel::increment,
        onDecrement = viewModel::decrement,
        onCheckoutClick = onCheckoutClick
    )
}

@Composable
private fun CartScreen(
    isLoading: Boolean,
    lines: List<CartLineUiModel>,
    total: Double,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    onCheckoutClick: () -> Unit
) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        lines.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                Text(text = "Your cart is empty", fontSize = 14.sp, color = PowerfyTextSecondary)
            }
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(lines) { line ->
                        CartLineItem(
                            product = line.product,
                            quantity = line.quantity,
                            onIncrement = { onIncrement(line.product.id) },
                            onDecrement = { onDecrement(line.product.id) }
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().background(PowerfyBorder).padding(top = 1.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Total: $${"%.2f".format(total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

                PrimaryButton(
                    text = "CHECKOUT",
                    onClick = onCheckoutClick,
                    modifier = Modifier.padding(bottom = 30.dp)
                )
            }
        }
    }
}