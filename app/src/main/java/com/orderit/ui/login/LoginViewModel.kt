package com.orderit.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orderit.core.result.onErr
import com.orderit.core.result.onOk
import com.orderit.data.local.SessionStore
import com.orderit.domain.repository.WaiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val waiterRepository: WaiterRepository,
    private val sessionStore: SessionStore
) : ViewModel() {

    data class LoginUiState(
        val username: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Unit>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, error = null) }
    }

    fun login() {
        val username = _uiState.value.username.trim()
        if (username.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu usuario") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            waiterRepository.getWaiters()
                .onOk { waiters ->
                    val match = waiters.find { it.username == username }
                    if (match != null) {
                        sessionStore.currentWaiterId = match.id
                        sessionStore.currentWaiterName = match.name
                        sessionStore.currentWaiterUsername = match.username
                        _loginSuccess.emit(Unit)
                    } else {
                        _uiState.update { it.copy(error = "Usuario no encontrado", isLoading = false) }
                    }
                }
                .onErr { _, msg ->
                    _uiState.update { it.copy(error = msg ?: "Error de conexion", isLoading = false) }
                }
        }
    }
}
