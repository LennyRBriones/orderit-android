package com.orderit.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.orderit.core.util.formatDate
import com.orderit.data.model.ProductDto
import com.orderit.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Date
        Text(
            text = formatDate(),
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )

        Spacer(Modifier.height(20.dp))

        // Summary cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryCard(
                title = "Total de ordenes",
                value = "${uiState.totalOrders}",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Total de ventas",
                value = "$${String.format("%,.0f", uiState.totalSales)}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Status cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatusCard("Ordenes nuevas", uiState.newOrders, StatusNew, Modifier.weight(1f))
            StatusCard("Ordenes pendientes", uiState.pendingOrders, StatusPending, Modifier.weight(1f))
            StatusCard("Ordenes servidas", uiState.servedOrders, StatusInTransit, Modifier.weight(1f))
            StatusCard("Ordenes pagadas", uiState.completedOrders, StatusCompleted, Modifier.weight(1f))
        }

        Spacer(Modifier.height(28.dp))

        // Featured products
        if (uiState.featuredProducts.isNotEmpty()) {
            Text(
                text = "Lo mas vendido hoy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.featuredProducts) { product ->
                    FeaturedProductCard(product)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FeaturedProductCard(product: ProductDto) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Column {
            // Image
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(BackgroundGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imagen", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }

            Column(Modifier.padding(12.dp)) {
                // Tag
                if (product.featured) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = SaleTag.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "#Destacado",
                            style = MaterialTheme.typography.labelSmall,
                            color = SaleTag,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                }

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (product.description.isNotBlank()) {
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (product.salePrice != null) {
                        Text(
                            text = "$${String.format("%.0f", product.salePrice)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SaleTag
                        )
                    } else {
                        Text(
                            text = "$${String.format("%.0f", product.price)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
