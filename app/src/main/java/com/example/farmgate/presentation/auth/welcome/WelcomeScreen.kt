package com.example.farmgate.presentation.auth.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import com.example.farmgate.ui.theme.FarmGateTheme

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.welcomhero),
            contentDescription = "Welcome hero",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopCenter
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 18.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.92f),
            tonalElevation = 2.dp
        ) {
            RowPill()
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
                ),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE6E6E6))
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Fresh from the farm\nto your table",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Discover local farmers, order fresh\nproduce for pickup, and support your\ncommunity marketplace.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF7A7A7A)
                )

                Spacer(modifier = Modifier.height(18.dp))

                FarmGatePrimaryButton(
                    text = "Create Account",
                    onClick = onCreateAccountClick,
                    enabled = true,
                    isLoading = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmGateSecondaryButton(
                    text = "Login",
                    onClick = onLoginClick,
                    enabled = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "By continuing, you agree to our Terms of Service.",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF9A9A9A)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun RowPill() {
    Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.farmgate_logo),
            contentDescription = "FarmGate icon",
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = "FARMGATE",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF1A1A1A)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    FarmGateTheme {
        WelcomeScreen(
            onLoginClick = {},
            onCreateAccountClick = {}
        )
    }
}