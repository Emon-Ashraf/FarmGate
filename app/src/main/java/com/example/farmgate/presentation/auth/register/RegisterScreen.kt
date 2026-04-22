package com.example.farmgate.presentation.auth.register

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGateLabeledTextField
import com.example.farmgate.presentation.components.FarmGatePasswordField
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.ui.theme.FarmGateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onFullNameChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRoleChanged: (Int) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    val green = Color(0xFF18D66B)
    val bg = Color(0xFFF7F7F7)
    val isFarmer = uiState.role == 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.farmgate_logo),
                        contentDescription = "Logo",
                        tint = green,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FarmGate",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1A1A1A)
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackToLoginClick,
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back to login",
                        tint = Color(0xFF1A1A1A)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ricefield),
                    contentDescription = "Register hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Join the movement",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Create your Account",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Fresh produce, directly from the source.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5C7A66)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Register as",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            RoleSelector(
                selectedRole = uiState.role,
                onRoleChanged = onRoleChanged,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(14.dp))

            FarmGateLabeledTextField(
                label = "Full Name",
                value = uiState.fullName,
                onValueChange = onFullNameChanged,
                placeholder = "Example: Ashraful Islam",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading
            )

            if (isFarmer) {
                Spacer(modifier = Modifier.height(14.dp))

                FarmGateLabeledTextField(
                    label = "Display Name",
                    value = uiState.displayName,
                    onValueChange = onDisplayNameChanged,
                    placeholder = "Example: Green Valley Farm",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            FarmGateLabeledTextField(
                label = "Phone Number",
                value = uiState.phoneNumber,
                onValueChange = onPhoneNumberChanged,
                placeholder = "Example: 01900000000",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(14.dp))

            FarmGateLabeledTextField(
                label = "Email Address",
                value = uiState.email,
                onValueChange = onEmailChanged,
                placeholder = "Example: ashraful@example.com",
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Must be at least 8 characters.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5C7A66)
            )

            if (isFarmer) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Farmer registration requires a display name.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5C7A66)
                )
            }

            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FarmGatePrimaryButton(
                text = if (isFarmer) "Create Farmer Account" else "Create Account",
                onClick = onRegisterClick,
                enabled = !uiState.isLoading,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 42.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7A7A7A)
                )

                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = green,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !uiState.isLoading
                    ) {
                        onBackToLoginClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun RoleSelector(
    selectedRole: Int,
    onRoleChanged: (Int) -> Unit,
    enabled: Boolean
) {
    val green = Color(0xFF18D66B)
    val container = Color(0xFFEAF0EC)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = container
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoleChip(
                text = "Customer",
                selected = selectedRole == 1,
                enabled = enabled,
                onClick = { onRoleChanged(1) },
                modifier = Modifier.weight(1f),
                activeColor = green
            )

            Spacer(modifier = Modifier.width(8.dp))

            RoleChip(
                text = "Farmer",
                selected = selectedRole == 2,
                enabled = enabled,
                onClick = { onRoleChanged(2) },
                modifier = Modifier.weight(1f),
                activeColor = green
            )
        }
    }
}

@Composable
private fun RoleChip(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color
) {
    val background = if (selected) Color.White else Color.Transparent
    val border = if (selected) Color(0xFFE1E7E4) else Color.Transparent
    val textColor = if (selected) Color(0xFF1A1A1A) else Color(0xFF5C7A66)

    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = background,
        border = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (enabled) textColor else Color(0xFF9A9A9A)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    FarmGateTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            onFullNameChanged = {},
            onPhoneNumberChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onRoleChanged = {},
            onDisplayNameChanged = {},
            onRegisterClick = {},
            onBackToLoginClick = {},
            onNavigation = {}
        )
    }
}