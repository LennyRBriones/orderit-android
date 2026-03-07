package com.orderit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String = "",
    val imageUrl: String = "",
    val categoryId: Int,
    val type: String = "individual",
    val price: Double,
    val salePrice: Double? = null,
    val saleStart: String? = null,
    val saleEnd: String? = null,
    val stock: Int = 0,
    val available: Boolean = true,
    val featured: Boolean = false
)

@Serializable
data class CreateProductRequest(
    val name: String,
    val description: String = "",
    val imageUrl: String = "",
    val categoryId: Int,
    val type: String = "individual",
    val price: Double,
    val salePrice: Double? = null,
    val saleStart: String? = null,
    val saleEnd: String? = null,
    val stock: Int = 0,
    val available: Boolean = true,
    val featured: Boolean = false
)
