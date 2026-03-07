package com.orderit.domain.repository

import com.orderit.core.result.AppResult
import com.orderit.data.model.WaiterDto

interface WaiterRepository {
    suspend fun getWaiters(): AppResult<List<WaiterDto>>
}
