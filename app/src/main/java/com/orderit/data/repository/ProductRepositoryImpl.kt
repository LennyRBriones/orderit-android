package com.orderit.data.repository

import com.orderit.core.network.OrderItApi
import com.orderit.core.result.AppResult
import com.orderit.core.result.safeApiCall
import com.orderit.data.model.CreateProductRequest
import com.orderit.data.model.ProductDto
import com.orderit.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: OrderItApi
) : ProductRepository {
    override suspend fun getProducts(): AppResult<List<ProductDto>> = safeApiCall { api.getProducts() }
    override suspend fun getProductsByCategory(categoryId: Int): AppResult<List<ProductDto>> = safeApiCall { api.getProductsByCategory(categoryId) }
    override suspend fun createProduct(request: CreateProductRequest): AppResult<ProductDto> = safeApiCall { api.createProduct(request) }
    override suspend fun updateProduct(id: Int, request: CreateProductRequest): AppResult<ProductDto> = safeApiCall { api.updateProduct(id, request) }
    override suspend fun deleteProduct(id: Int): AppResult<Unit> = safeApiCall { api.deleteProduct(id) }
}
