package com.example.farmgate.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGateLabeledTextField
import com.example.farmgate.presentation.components.FarmGatePasswordField
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.ui.theme.FarmGateTheme
import androidx.compose.foundation.interaction.MutableInteractionSource as FoundationMutableInteractionSource

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFDFF7EA)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.farmgate_logo),
                    contentDescription = "Logo",
                    tint = Color(0xFF18D66B),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Fresh from the farm to you. Log in to\nmanage your orders.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        FarmGateLabeledTextField(
            label = "Phone or Email",
            value = uiState.login,
            onValueChange = onLoginChanged,
            placeholder = "Enter your phone or email",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(14.dp))

        FarmGatePasswordField(
            label = "Password",
            value = uiState.password,
            onValueChange = onPasswordChanged,
            placeholder = "Enter your password",
            enabled = !uiState.isLoading,
            imeAction = ImeAction.Done
        )

        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        FarmGatePrimaryButton(
            text = "Log In",
            onClick = onLoginClick,
            enabled = !uiState.isLoading,
            isLoading = uiState.isLoading,
            modifier = Modifier.height(54.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(bottom = 72.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7A7A7A)
            )

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF18D66B),
                modifier = Modifier.clickable(
                    interactionSource = remember { FoundationMutableInteractionSource() },
                    indication = null,
                    enabled = !uiState.isLoading
                ) {
                    onRegisterClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    FarmGateTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onLoginChanged = {},
            onPasswordChanged = {},
            onLoginClick = {},
            onRegisterClick = {},
            onNavigation = {}
        )
    }
}