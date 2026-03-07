package com.orderit.ui.metrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orderit.core.result.AppResult
import com.orderit.core.result.onErr
import com.orderit.core.result.onOk
import com.orderit.domain.repository.OrderRepository
import com.orderit.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopProduct(val name: String, val quantity: Int)

@HiltViewModel
class MetricsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    data class MetricsUiState(
        val isLoading: Boolean = true,
        val totalOrders: Int = 0,
        val totalRevenue: Double = 0.0,
        val averageOrderValue: Double = 0.0,
        val ordersByStatus: Map<String, Int> = emptyMap(),
        val topProducts: List<TopProduct> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(MetricsUiState())
    val uiState: StateFlow<MetricsUiState> = _uiState.asStateFlow()

    init {
        loadMetrics()
    }

    fun loadMetrics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val ordersResult = orderRepository.getOrders()
            val productsResult = productRepository.getProducts()

            ordersResult.onOk { orders ->
                val total = orders.sumOf { it.total }
                val avg = if (orders.isNotEmpty()) total / orders.size else 0.0
                val byStatus = orders.groupBy { it.status }.mapValues { it.value.size }

                // Load all order details for top products
                val productsMap = when (productsResult) {
                    is AppResult.Ok -> productsResult.data.associateBy { it.id }
                    is AppResult.Err -> emptyMap()
                }

                val allDetails = orders.map { order ->
                    async { orderRepository.getOrderDetails(order.id) }
                }.awaitAll()

                val productCounts = mutableMapOf<Int, Int>()
                allDetails.forEach { result ->
                    if (result is AppResult.Ok) {
                        result.data.forEach { detail ->
                            productCounts[detail.productId] = (productCounts[detail.productId] ?: 0) + detail.quantity
                        }
                    }
                }

                val topProducts = productCounts.entries
                    .sortedByDescending { it.value }
                    .take(5)
                    .map { (productId, qty) ->
                        TopProduct(
                            name = productsMap[productId]?.name ?: "Producto #$productId",
                            quantity = qty
                        )
                    }

                _uiState.update {
                    it.copy(
                        totalOrders = orders.size,
                        totalRevenue = total,
                        averageOrderValue = avg,
                        ordersByStatus = byStatus,
                        topProducts = topProducts,
                        isLoading = false
                    )
                }
            }.onErr { _, msg ->
                _uiState.update { it.copy(error = msg, isLoading = false) }
            }
        }
    }
}
