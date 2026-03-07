package com.orderit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WaiterDto(
    val id: Int,
    val name: String,
    val username: String
)
