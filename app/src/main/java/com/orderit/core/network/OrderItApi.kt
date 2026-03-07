package com.orderit.core.network

import com.orderit.data.model.*
import retrofit2.http.*

interface OrderItApi {

    // Waiters
    @GET("waiters")
    suspend fun getWaiters(): List<WaiterDto>

    @GET("waiters/{id}")
    suspend fun getWaiter(@Path("id") id: Int): WaiterDto

    // Categories
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("categories/{id}")
    suspend fun getCategory(@Path("id") id: Int): CategoryDto

    // Products
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto

    @GET("categories/{categoryId}/products")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: Int): List<ProductDto>

    @POST("products")
    suspend fun createProduct(@Body request: CreateProductRequest): ProductDto

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body request: CreateProductRequest): ProductDto

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int)

    // Dining Tables
    @GET("tables")
    suspend fun getTables(): List<DiningTableDto>

    @GET("tables/{id}")
    suspend fun getTable(@Path("id") id: Int): DiningTableDto

    // Orders
    @GET("orders")
    suspend fun getOrders(): List<OrderDto>

    @GET("orders/{id}")
    suspend fun getOrder(@Path("id") id: Int): OrderDto

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderDto

    @PUT("orders/{id}")
    suspend fun updateOrder(@Path("id") id: Int, @Body request: CreateOrderRequest): OrderDto

    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int)

    // Order Details
    @GET("orders/{orderId}/details")
    suspend fun getOrderDetails(@Path("orderId") orderId: Int): List<OrderDetailDto>

    @POST("orders/{orderId}/details")
    suspend fun createOrderDetail(
        @Path("orderId") orderId: Int,
        @Body request: CreateOrderDetailRequest
    ): OrderDetailDto

    @DELETE("orders/{orderId}/details/{id}")
    suspend fun deleteOrderDetail(@Path("orderId") orderId: Int, @Path("id") id: Int)
}
