package se.tattooink.powerfy.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import se.tattooink.powerfy.R
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.components.SecondaryButton
import se.tattooink.powerfy.ui.components.requestGoogleIdToken
import se.tattooink.powerfy.ui.theme.PowerfyBorder
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (uiState.loginSucceeded) {
        onLoginSuccess()
    }

    LoginScreen(
        email = uiState.email,
        password = uiState.password,
        errorMessage = uiState.errorMessage,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onGoogleClick = {
            coroutineScope.launch {
                val tokenResult = requestGoogleIdToken(context)
                tokenResult.fold(
                    onSuccess = { idToken -> viewModel.loginWithGoogle(idToken) },
                    onFailure = { error -> viewModel.showGoogleError(error) }
                )
            }
        },
        suggestSignUp = uiState.suggestSignUp,
        onSignUpClick = onSignUpClick
    )
}

@Composable
private fun LoginScreen(
    email: String,
    password: String,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    suggestSignUp: Boolean,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(PowerfyPrimary)
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logobig),
                contentDescription = null,
                modifier = Modifier.height(80.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .offset(y = (-24).dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .padding(horizontal = 34.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome back",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Log in to continue shopping the best deals in electronics.",
                fontSize = 12.sp,
                color = PowerfyTextSecondary
            )

            AuthField(
                label = "Email",
                value = email,
                onValueChange = onEmailChange,
                placeholder = "you@example.com",
                keyboardType = KeyboardType.Email
            )

            AuthField(
                label = "Password",
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PrimaryButton(
                    text = "Log In",
                    onClick = onLoginClick
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Forgot password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = PowerfyPrimary
                    )
                }
            }

            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(PowerfyBorder)
                    )
                    Text(
                        text = "or continue with",
                        fontSize = 11.sp,
                        color = PowerfyTextSecondary
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(PowerfyBorder)
                    )
                }
            }

            SecondaryButton(
                text = "Continue with Google",
                onClick = onGoogleClick
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            if (suggestSignUp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSignUpClick),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? Sign Up",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = PowerfyPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = PowerfyTextSecondary
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, fontSize = 13.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = PowerfyBorder,
                focusedIndicatorColor = PowerfyPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}