package com.orderit.domain.repository

import com.orderit.core.result.AppResult
import com.orderit.data.model.*

interface OrderRepository {
    suspend fun getOrders(): AppResult<List<OrderDto>>
    suspend fun getOrder(id: Int): AppResult<OrderDto>
    suspend fun createOrder(request: CreateOrderRequest): AppResult<OrderDto>
    suspend fun getOrderDetails(orderId: Int): AppResult<List<OrderDetailDto>>
    suspend fun createOrderDetail(orderId: Int, request: CreateOrderDetailRequest): AppResult<OrderDetailDto>
    suspend fun deleteOrderDetail(orderId: Int, detailId: Int): AppResult<Unit>
}
