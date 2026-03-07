package com.orderit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailDto(
    val id: Int,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

@Serializable
data class CreateOrderDetailRequest(
    val orderId: Int,
    val productId: Int,
    val quantity: Int
)
