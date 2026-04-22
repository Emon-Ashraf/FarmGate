package com.example.farmgate.presentation.customer.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.core.navigation.Routes

data class CustomerBottomNavItem(
    val route: String,
    val iconRes: Int,
    val contentDescription: String
)

@Composable
fun CustomerBottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        CustomerBottomNavItem(
            route = Routes.CUSTOMER_HOME,
            iconRes = R.drawable.ic_home,
            contentDescription = "Home"
        ),
        CustomerBottomNavItem(
            route = Routes.CUSTOMER_ORDERS,
            iconRes = R.drawable.ic_orders,
            contentDescription = "Orders"
        ),
        CustomerBottomNavItem(
            route = Routes.CUSTOMER_PROFILE,
            iconRes = R.drawable.ic_profile,
            contentDescription = "Profile"
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.contentDescription,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = null,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF18D66B),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color(0x1A18D66B)
                )
            )
        }
    }
}