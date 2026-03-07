package com.orderit.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orderit.core.result.AppResult
import com.orderit.core.result.onErr
import com.orderit.core.result.onOk
import com.orderit.data.model.ProductDto
import com.orderit.domain.model.OrderWithDetails
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

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    data class OrdersUiState(
        val isLoading: Boolean = true,
        val orders: List<OrderWithDetails> = emptyList(),
        val selectedOrder: OrderWithDetails? = null,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private var productsCache: Map<Int, ProductDto> = emptyMap()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load products for name resolution
            if (productsCache.isEmpty()) {
                productRepository.getProducts().onOk { products ->
                    productsCache = products.associateBy { it.id }
                }
            }

            orderRepository.getOrders()
                .onOk { orders ->
                    val ordersWithDetails = orders.map { order ->
                        async {
                            val details = orderRepository.getOrderDetails(order.id)
                            val detailsList = when (details) {
                                is AppResult.Ok -> details.data
                                is AppResult.Err -> emptyList()
                            }
                            OrderWithDetails(
                                order = order,
                                details = detailsList,
                                products = productsCache
                            )
                        }
                    }.awaitAll()

                    _uiState.update {
                        it.copy(
                            orders = ordersWithDetails.sortedByDescending { o -> o.order.openedAt },
                            isLoading = false
                        )
                    }
                }
                .onErr { _, msg ->
                    _uiState.update { it.copy(error = msg, isLoading = false) }
                }
        }
    }

    fun selectOrder(order: OrderWithDetails) {
        _uiState.update { it.copy(selectedOrder = order) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedOrder = null) }
    }
}
