package com.orderit.data.repository

import com.orderit.core.network.OrderItApi
import com.orderit.core.result.AppResult
import com.orderit.core.result.safeApiCall
import com.orderit.data.model.WaiterDto
import com.orderit.domain.repository.WaiterRepository
import javax.inject.Inject

class WaiterRepositoryImpl @Inject constructor(
    private val api: OrderItApi
) : WaiterRepository {
    override suspend fun getWaiters(): AppResult<List<WaiterDto>> = safeApiCall { api.getWaiters() }
}
