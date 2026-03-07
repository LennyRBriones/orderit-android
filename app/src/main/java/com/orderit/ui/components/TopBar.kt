package com.orderit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orderit.ui.theme.BackgroundGray
import com.orderit.ui.theme.TextSecondary

@Composable
fun OrderItTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text("Buscador", color = TextSecondary)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = TextSecondary
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = BackgroundGray,
                focusedContainerColor = BackgroundGray,
                unfocusedBorderColor = BackgroundGray,
                focusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .heightIn(max = 53.dp)
        )

        Spacer(Modifier.width(16.dp))

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
