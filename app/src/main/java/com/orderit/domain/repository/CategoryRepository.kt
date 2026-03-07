package com.orderit.domain.repository

import com.orderit.core.result.AppResult
import com.orderit.data.model.CategoryDto

interface CategoryRepository {
    suspend fun getCategories(): AppResult<List<CategoryDto>>
}
