package com.orderit.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orderit.data.model.CategoryDto
import com.orderit.data.model.CreateProductRequest
import com.orderit.data.model.ProductDto
import com.orderit.ui.theme.StatusNew
import com.orderit.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormPanel(
    editingProduct: ProductDto?,
    categories: List<CategoryDto>,
    isSaving: Boolean,
    onSave: (CreateProductRequest) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(editingProduct) { mutableStateOf(editingProduct?.name ?: "") }
    var description by remember(editingProduct) { mutableStateOf(editingProduct?.description ?: "") }
    var type by remember(editingProduct) { mutableStateOf(editingProduct?.type ?: "individual") }
    var price by remember(editingProduct) { mutableStateOf(editingProduct?.price?.toString() ?: "") }
    var hasSalePrice by remember(editingProduct) { mutableStateOf(editingProduct?.salePrice != null) }
    var salePrice by remember(editingProduct) { mutableStateOf(editingProduct?.salePrice?.toString() ?: "") }
    var selectedCategoryId by remember(editingProduct) { mutableIntStateOf(editingProduct?.categoryId ?: (categories.firstOrNull()?.id ?: 0)) }
    var imageUrl by remember(editingProduct) { mutableStateOf(editingProduct?.imageUrl ?: "") }
    var available by remember(editingProduct) { mutableStateOf(editingProduct?.available ?: true) }

    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (editingProduct != null) "Editar producto" else "Nuevo producto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .padding(end = 0.dp)
                    )
                    Text(
                        text = if (editingProduct != null) "Editando" else "Nuevo",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusNew
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Image URL
            Text("URL de imagen", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                placeholder = { Text("https://...") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Name
            Text("Nombre del producto", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nombre") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Description
            Text("Descripcion", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Descripcion del producto") },
                minLines = 2,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Type
            Text("Tipo (Combo o individual)", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = type == "individual",
                    onClick = { type = "individual" },
                    label = { Text("Individual") }
                )
                FilterChip(
                    selected = type == "combo",
                    onClick = { type = "combo" },
                    label = { Text("Combo") }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Category
            Text("Categoria", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategoryId == cat.id,
                        onClick = { selectedCategoryId = cat.id },
                        label = { Text(cat.name) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Price
            Text("Precio normal", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                prefix = { Text("$") },
                placeholder = { Text("0.00") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Sale price
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasSalePrice, onCheckedChange = { hasSalePrice = it })
                Text("Precio de oferta", style = MaterialTheme.typography.bodyMedium)
            }

            if (hasSalePrice) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = salePrice,
                    onValueChange = { salePrice = it },
                    prefix = { Text("$") },
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Available
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = available, onCheckedChange = { available = it })
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (available) "Habilitado" else "Deshabilitado",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(24.dp))

            // Save button
            Button(
                onClick = {
                    val priceVal = price.toDoubleOrNull() ?: 0.0
                    val salePriceVal = if (hasSalePrice) salePrice.toDoubleOrNull() else null
                    onSave(
                        CreateProductRequest(
                            name = name,
                            description = description,
                            imageUrl = imageUrl,
                            categoryId = selectedCategoryId,
                            type = type,
                            price = priceVal,
                            salePrice = salePriceVal,
                            available = available
                        )
                    )
                },
                enabled = name.isNotBlank() && price.isNotBlank() && !isSaving,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                } else {
                    Text(if (editingProduct != null) "Guardar cambios" else "Crear producto")
                }
            }
        }
    }
}
