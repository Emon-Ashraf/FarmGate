package com.example.farmgate.presentation.customer.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.core.navigation.Routes

data class CustomerBottomNavItem(
    val route: String,
    val iconRes: Int,
    val label: String,
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
            label = "Home",
            contentDescription = "Home"
        ),
        CustomerBottomNavItem(
            route = Routes.CUSTOMER_ORDERS,
            iconRes = R.drawable.ic_orders,
            label = "Orders",
            contentDescription = "Orders"
        ),
        CustomerBottomNavItem(
            route = Routes.CUSTOMER_PROFILE,
            iconRes = R.drawable.ic_profile,
            label = "Profile",
            contentDescription = "Profile"
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 2.dp, vertical = 0.dp),
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 8.dp,
            bottomEnd = 8.dp
        ),
        //color = MaterialTheme.colorScheme.surface,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
        //
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.10f)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                CustomerBottomNavBarItem(
                    item = item,
                    selected = selected,
                    onClick = { onItemClick(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CustomerBottomNavBarItem(
    item: CustomerBottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF18D66B)
    val selectedBackground = Color(0x1A5A9677)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = if (selected) selectedBackground else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.contentDescription,
            modifier = Modifier.size(23.dp),
            tint = if (selected) {
                selectedColor
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (selected) {
                selectedColor
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CustomerBottomNavBarPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            CustomerBottomNavBar(
                currentRoute = Routes.CUSTOMER_HOME,
                onItemClick = {}
            )
        }
    }
}