package com.orderit.domain.model

import com.orderit.data.model.OrderDetailDto
import com.orderit.data.model.OrderDto
import com.orderit.data.model.ProductDto

data class OrderWithDetails(
    val order: OrderDto,
    val details: List<OrderDetailDto> = emptyList(),
    val products: Map<Int, ProductDto> = emptyMap()
) {
    val subtotal: Double get() = details.sumOf { it.subtotal }
    val totalItems: Int get() = details.sumOf { it.quantity }
}
