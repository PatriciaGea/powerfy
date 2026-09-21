package se.tattooink.powerfy.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import kotlinx.coroutines.launch
import se.tattooink.powerfy.ui.components.BackButton
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.theme.PowerfyBorder
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfySurface
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary
import se.tattooink.powerfy.util.toSekPrice

@Composable
fun CheckoutRoute(
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> onPaymentSuccess()
            is PaymentSheetResult.Canceled -> Unit
            is PaymentSheetResult.Failed -> errorMessage = "Payment failed: ${result.error.message}"
        }
    }

    CheckoutScreen(
        customerName = uiState.customerName,
        subtotal = uiState.subtotal,
        vat = uiState.vat,
        selectedMethod = uiState.selectedMethod,
        total = uiState.total,
        errorMessage = errorMessage,
        onMethodSelected = viewModel::selectDeliveryMethod,
        onBackClick = onBackClick,
        onContinueToPaymentClick = {
            coroutineScope.launch {
                val result = viewModel.createPaymentIntent()
                result.fold(
                    onSuccess = { clientSecret ->
                        paymentSheet.presentWithPaymentIntent(
                            clientSecret,
                            PaymentSheet.Configuration(merchantDisplayName = "Powerfy")
                        )
                    },
                    onFailure = { error ->
                        errorMessage = error.message ?: "Could not start payment"
                    }
                )
            }
        }
    )
}

@Composable
private fun CheckoutScreen(
    customerName: String,
    subtotal: Double,
    vat: Double,
    selectedMethod: DeliveryMethod,
    total: Double,
    errorMessage: String?,
    onMethodSelected: (DeliveryMethod) -> Unit,
    onBackClick: () -> Unit,
    onContinueToPaymentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BackButton(onClick = onBackClick)
            Text(text = "Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Text(text = "DELIVERY ADDRESS", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PowerfyTextSecondary)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(PowerfySurface)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = customerName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "Edit", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = PowerfyPrimary)
            }
            Text(text = "Hyper Island, Stockholm, 11122, Sweden", fontSize = 12.sp, color = PowerfyTextSecondary)
            Text(text = "+46 70 123 45 67", fontSize = 12.sp, color = PowerfyTextSecondary)
        }

        Text(text = "DELIVERY METHOD", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PowerfyTextSecondary)

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DeliveryOption(
                method = DeliveryMethod.STANDARD,
                isSelected = selectedMethod == DeliveryMethod.STANDARD,
                onClick = { onMethodSelected(DeliveryMethod.STANDARD) }
            )
            DeliveryOption(
                method = DeliveryMethod.EXPRESS,
                isSelected = selectedMethod == DeliveryMethod.EXPRESS,
                onClick = { onMethodSelected(DeliveryMethod.EXPRESS) }
            )
        }

        Text(text = "ORDER SUMMARY", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PowerfyTextSecondary)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(PowerfySurface)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryRow(label = "Subtotal", value = subtotal.toSekPrice())
            SummaryRow(label = "Delivery", value = selectedMethod.fee.toSekPrice())
            SummaryRow(label = "VAT (25%)", value = vat.toSekPrice())
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PowerfyBorder))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = total.toSekPrice(), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PowerfyPrimary)
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        PrimaryButton(text = "CONTINUE TO PAYMENT", onClick = onContinueToPaymentClick)
    }
}

@Composable
private fun DeliveryOption(method: DeliveryMethod, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFE3F2FC) else PowerfySurface)
            .let {
                if (isSelected) it.border(2.dp, PowerfyPrimary, RoundedCornerShape(12.dp)) else it
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) PowerfyPrimary else Color.White)
                    .border(1.5.dp, if (isSelected) PowerfyPrimary else PowerfyBorder, CircleShape)
            )
            Column {
                Text(text = method.label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = method.subtitle, fontSize = 11.sp, color = PowerfyTextSecondary)
            }
        }
        Text(
            text = method.fee.toSekPrice(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PowerfyPrimary else Color.Black
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 13.sp, color = Color.Black)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}