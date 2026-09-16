package se.tattooink.powerfy.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import se.tattooink.powerfy.ui.components.BackButton
import se.tattooink.powerfy.ui.components.PrimaryButton
import se.tattooink.powerfy.ui.components.SecondaryButton
import se.tattooink.powerfy.ui.components.requestGoogleIdToken
import se.tattooink.powerfy.ui.theme.PowerfyBorder
import se.tattooink.powerfy.ui.theme.PowerfyPrimary
import se.tattooink.powerfy.ui.theme.PowerfyTextSecondary

@Composable
fun SignUpRoute(
    onSignUpSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (uiState.signUpSucceeded) {
        onSignUpSuccess()
    }

    SignUpScreen(
        fullName = uiState.fullName,
        email = uiState.email,
        password = uiState.password,
        confirmPassword = uiState.confirmPassword,
        errorMessage = uiState.errorMessage,
        onFullNameChange = viewModel::onFullNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onCreateAccountClick = viewModel::signUp,
        onGoogleClick = {
            coroutineScope.launch {
                val tokenResult = requestGoogleIdToken(context)
                tokenResult.onSuccess { idToken ->
                    viewModel.signUpWithGoogle(idToken)
                }
            }
        },
        onBackClick = onBackClick,
        onLoginClick = onLoginClick
    )
}

@Composable
private fun SignUpScreen(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    errorMessage: String?,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onCreateAccountClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BackButton(onClick = onBackClick)

        Image(
            painter = painterResource(id = R.drawable.plugso),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = "Sign up",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PowerfyPrimary
        )

        Text(
            text = "Create an account to start shopping the best electronics deals.",
            fontSize = 12.sp,
            color = PowerfyTextSecondary
        )

        SignUpField(
            label = "Full name",
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = "Your name here",
            keyboardType = KeyboardType.Text
        )

        SignUpField(
            label = "Email address",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "you@example.com",
            keyboardType = KeyboardType.Email
        )

        SignUpField(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "••••••••",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        SignUpField(
            label = "Re-enter password",
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "••••••••",
            keyboardType = KeyboardType.Password,
            isPassword = true
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

        PrimaryButton(
            text = "Create Account",
            onClick = onCreateAccountClick
        )

        SecondaryButton(
            text = "Create Account with Google",
            onClick = onGoogleClick,
            iconResId = R.drawable.googlelogo
        )

        Text(
            text = "Already have an account? Log In",
            fontSize = 12.sp,
            color = PowerfyTextSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable(onClick = onLoginClick),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SignUpField(
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