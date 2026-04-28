package com.example.farmgate.presentation.farmer.navigation

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

data class FarmerBottomNavItem(
    val route: String,
    val iconRes: Int,
    val label: String,
    val contentDescription: String
)

@Composable
fun FarmerBottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        FarmerBottomNavItem(
            route = Routes.FARMER_DASHBOARD,
            iconRes = R.drawable.ic_home,
            label = "Home",
            contentDescription = "Home"
        ),
        FarmerBottomNavItem(
            route = Routes.FARMER_ORDERS,
            iconRes = R.drawable.ic_orders,
            label = "Orders",
            contentDescription = "Orders"
        ),
        FarmerBottomNavItem(
            route = Routes.FARMER_PRODUCTS,
            iconRes = R.drawable.ic_products,
            label = "Products",
            contentDescription = "Products"
        ),
        FarmerBottomNavItem(
            route = Routes.FARMER_PROFILE,
            iconRes = R.drawable.ic_profile,
            label = "Profile",
            contentDescription = "Profile"
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                FarmerBottomNavBarItem(
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
private fun FarmerBottomNavBarItem(
    item: FarmerBottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF18D66B)
    val selectedBackground = Color(0x1A18D66B)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                color = if (selected) selectedBackground else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
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
            tint = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}