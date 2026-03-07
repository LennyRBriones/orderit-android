package com.orderit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DiningTableDto(
    val id: Int,
    val number: Int,
    val capacity: Int,
    val status: String = "free"
)
