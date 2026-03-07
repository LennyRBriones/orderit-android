package com.orderit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orderit.ui.theme.SidebarDark
import com.orderit.ui.theme.SidebarSelected
import com.orderit.ui.theme.TextMuted
import com.orderit.ui.theme.TextOnDark

enum class SideBarItem(val label: String, val icon: ImageVector) {
    Dashboard("Dashboard", Icons.Default.Dashboard),
    Ordenes("Ordenes", Icons.Default.Receipt),
    Menu("Menu", Icons.Default.Restaurant),
    Metricas("Metricas", Icons.Default.BarChart)
}

@Composable
fun SideBar(
    selectedItem: SideBarItem,
    onItemSelected: (SideBarItem) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(SidebarDark)
            .padding(vertical = 24.dp, horizontal = 12.dp)
    ) {
        // Logo
        Text(
            text = "OrderIt",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFE53935),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Nav items
        SideBarItem.entries.forEach { item ->
            val isSelected = item == selectedItem
            SideBarNavItem(
                item = item,
                isSelected = isSelected,
                onClick = { onItemSelected(item) }
            )
            Spacer(Modifier.height(4.dp))
        }

        Spacer(Modifier.weight(1f))

        // Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onLogout)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Cerrar sesion",
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Cerrar sesion",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun SideBarNavItem(
    item: SideBarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) SidebarSelected else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isSelected) TextOnDark else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) TextOnDark else TextMuted,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
