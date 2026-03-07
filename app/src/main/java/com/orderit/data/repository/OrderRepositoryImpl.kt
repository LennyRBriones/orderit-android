package com.orderit.data.repository

import com.orderit.core.network.OrderItApi
import com.orderit.core.result.AppResult
import com.orderit.core.result.safeApiCall
import com.orderit.data.model.*
import com.orderit.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderItApi
) : OrderRepository {
    override suspend fun getOrders(): AppResult<List<OrderDto>> = safeApiCall { api.getOrders() }
    override suspend fun getOrder(id: Int): AppResult<OrderDto> = safeApiCall { api.getOrder(id) }
    override suspend fun createOrder(request: CreateOrderRequest): AppResult<OrderDto> = safeApiCall { api.createOrder(request) }
    override suspend fun getOrderDetails(orderId: Int): AppResult<List<OrderDetailDto>> = safeApiCall { api.getOrderDetails(orderId) }
    override suspend fun createOrderDetail(orderId: Int, request: CreateOrderDetailRequest): AppResult<OrderDetailDto> = safeApiCall { api.createOrderDetail(orderId, request) }
    override suspend fun deleteOrderDetail(orderId: Int, detailId: Int): AppResult<Unit> = safeApiCall { api.deleteOrderDetail(orderId, detailId) }
}
