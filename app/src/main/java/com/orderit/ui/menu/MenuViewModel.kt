package com.orderit.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orderit.core.result.onErr
import com.orderit.core.result.onOk
import com.orderit.data.model.CategoryDto
import com.orderit.data.model.CreateProductRequest
import com.orderit.data.model.ProductDto
import com.orderit.domain.repository.CategoryRepository
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
class MenuViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    data class MenuUiState(
        val isLoading: Boolean = true,
        val categories: List<CategoryDto> = emptyList(),
        val allProducts: List<ProductDto> = emptyList(),
        val selectedCategoryId: Int? = null,
        val showProductForm: Boolean = false,
        val editingProduct: ProductDto? = null,
        val isSaving: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    val filteredProducts: List<ProductDto>
        get() {
            val state = _uiState.value
            return if (state.selectedCategoryId == null) {
                state.allProducts
            } else {
                state.allProducts.filter { it.categoryId == state.selectedCategoryId }
            }
        }

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val categoriesDeferred = async { categoryRepository.getCategories() }
            val productsDeferred = async { productRepository.getProducts() }

            categoriesDeferred.await().onOk { categories ->
                _uiState.update { it.copy(categories = categories) }
            }

            productsDeferred.await().onOk { products ->
                _uiState.update { it.copy(allProducts = products, isLoading = false) }
            }.onErr { _, msg ->
                _uiState.update { it.copy(error = msg, isLoading = false) }
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun openNewProductForm() {
        _uiState.update { it.copy(showProductForm = true, editingProduct = null) }
    }

    fun openEditProductForm(product: ProductDto) {
        _uiState.update { it.copy(showProductForm = true, editingProduct = product) }
    }

    fun closeForm() {
        _uiState.update { it.copy(showProductForm = false, editingProduct = null) }
    }

    fun saveProduct(request: CreateProductRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val editing = _uiState.value.editingProduct
            val result = if (editing != null) {
                productRepository.updateProduct(editing.id, request)
            } else {
                productRepository.createProduct(request)
            }

            result
                .onOk {
                    _uiState.update { it.copy(isSaving = false, showProductForm = false, editingProduct = null) }
                    loadMenu()
                }
                .onErr { _, msg ->
                    _uiState.update { it.copy(isSaving = false, error = msg) }
                }
        }
    }

    fun productCountForCategory(categoryId: Int): Int {
        return _uiState.value.allProducts.count { it.categoryId == categoryId }
    }
}
