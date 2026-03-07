package com.orderit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val tableId: Int,
    val waiterId: Int,
    val guestCount: Int,
    val status: String = "new",
    val total: Double = 0.0,
    val tip: Double = 0.0,
    val openedAt: String,
    val closedAt: String? = null
)

@Serializable
data class CreateOrderRequest(
    val tableId: Int,
    val waiterId: Int,
    val guestCount: Int
)
