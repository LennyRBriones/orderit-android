package com.orderit.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.orderit.data.model.ProductDto
import com.orderit.ui.theme.*

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(Modifier.fillMaxSize()) {
        // Category tabs
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All tab
                CategoryChip(
                    name = "Todos",
                    count = uiState.allProducts.size,
                    isSelected = uiState.selectedCategoryId == null,
                    onClick = { viewModel.selectCategory(null) }
                )
                uiState.categories.forEach { category ->
                    CategoryChip(
                        name = category.name,
                        count = viewModel.productCountForCategory(category.id),
                        isSelected = uiState.selectedCategoryId == category.id,
                        onClick = { viewModel.selectCategory(category.id) }
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Add product button
            FloatingActionButton(
                onClick = viewModel::openNewProductForm,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo producto", modifier = Modifier.size(20.dp))
            }
        }

        // Content
        Row(Modifier.fillMaxSize()) {
            // Product grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        categoryName = uiState.categories.find { it.id == product.categoryId }?.name ?: "",
                        onEdit = { viewModel.openEditProductForm(product) }
                    )
                }
            }

            // Product form panel
            AnimatedVisibility(
                visible = uiState.showProductForm,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                ProductFormPanel(
                    editingProduct = uiState.editingProduct,
                    categories = uiState.categories,
                    isSaving = uiState.isSaving,
                    onSave = viewModel::saveProduct,
                    onClose = viewModel::closeForm,
                    modifier = Modifier.width(400.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    name: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
        border = if (!isSelected) ButtonDefaults.outlinedButtonBorder(true) else null,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextPrimary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextMuted
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductDto,
    categoryName: String,
    onEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image
            Box {
                if (product.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(BackgroundGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin imagen", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Status badges
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.salePrice != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = SaleTag.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = "#Oferta",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (product.available) SuccessGreen.copy(alpha = 0.9f) else ErrorRed.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = if (product.available) "Habilitado" else "Deshabilitado",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Info
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Precio real:",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "$${String.format("%.2f", product.price)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (product.salePrice != null) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }
                        if (product.salePrice != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Precio de oferta:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "$${String.format("%.2f", product.salePrice)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SaleTag
                                )
                            }
                        }
                    }

                    TextButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            }
        }
    }
}
