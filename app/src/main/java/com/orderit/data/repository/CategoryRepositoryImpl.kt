package com.orderit.data.repository

import com.orderit.core.network.OrderItApi
import com.orderit.core.result.AppResult
import com.orderit.core.result.safeApiCall
import com.orderit.data.model.CategoryDto
import com.orderit.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: OrderItApi
) : CategoryRepository {
    override suspend fun getCategories(): AppResult<List<CategoryDto>> = safeApiCall { api.getCategories() }
}
