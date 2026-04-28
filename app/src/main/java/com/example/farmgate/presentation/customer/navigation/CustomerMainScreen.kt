package com.example.farmgate.presentation.customer.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.farmgate.R

@Composable
fun CustomerMainScreen(
    navController: NavHostController,
    cartItemCount: Int,
    onCartClick: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content(Modifier.fillMaxSize())

            if (cartItemCount > 0) {
                FloatingCartBar(
                    cartItemCount = cartItemCount,
                    onClick = onCartClick,
                    modifier = Modifier
                        //.align(Alignment.BottomCenter)
                        //.padding(horizontal = 18.dp, vertical = 12.dp)
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun FloatingCartBar(
    cartItemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.navigationBarsPadding(),
        shape = RoundedCornerShape(999.dp),
        color = Color(0xFFDE1759),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Color(0xFFFF3B30),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = if (cartItemCount > 9) "9+" else cartItemCount.toString()
                        )
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Cart",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Text(
                text = "Review order",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }
    }
}