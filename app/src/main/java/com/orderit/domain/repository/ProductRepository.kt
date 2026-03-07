package com.orderit.domain.repository

import com.orderit.core.result.AppResult
import com.orderit.data.model.CreateProductRequest
import com.orderit.data.model.ProductDto

interface ProductRepository {
    suspend fun getProducts(): AppResult<List<ProductDto>>
    suspend fun getProductsByCategory(categoryId: Int): AppResult<List<ProductDto>>
    suspend fun createProduct(request: CreateProductRequest): AppResult<ProductDto>
    suspend fun updateProduct(id: Int, request: CreateProductRequest): AppResult<ProductDto>
    suspend fun deleteProduct(id: Int): AppResult<Unit>
}
