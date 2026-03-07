package com.orderit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orderit.core.result.onErr
import com.orderit.core.result.onOk
import com.orderit.data.model.ProductDto
import com.orderit.domain.repository.OrderRepository
import com.orderit.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    data class DashboardUiState(
        val isLoading: Boolean = true,
        val totalOrders: Int = 0,
        val totalSales: Double = 0.0,
        val newOrders: Int = 0,
        val pendingOrders: Int = 0,
        val servedOrders: Int = 0,
        val completedOrders: Int = 0,
        val featuredProducts: List<ProductDto> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val ordersDeferred = async { orderRepository.getOrders() }
            val productsDeferred = async { productRepository.getProducts() }

            val ordersResult = ordersDeferred.await()
            val productsResult = productsDeferred.await()

            ordersResult.onOk { orders ->
                val total = orders.sumOf { it.total }
                _uiState.update {
                    it.copy(
                        totalOrders = orders.size,
                        totalSales = total,
                        newOrders = orders.count { o -> o.status == "new" },
                        pendingOrders = orders.count { o -> o.status == "in_preparation" },
                        servedOrders = orders.count { o -> o.status == "served" },
                        completedOrders = orders.count { o -> o.status == "paid" }
                    )
                }
            }.onErr { _, msg ->
                _uiState.update { it.copy(error = msg) }
            }

            productsResult.onOk { products ->
                _uiState.update {
                    it.copy(
                        featuredProducts = products.filter { p -> p.featured },
                        isLoading = false
                    )
                }
            }.onErr { _, _ ->
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
