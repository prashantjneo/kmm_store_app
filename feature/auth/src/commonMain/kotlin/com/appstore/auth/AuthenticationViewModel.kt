package com.appstore.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstore.data.domain.model.login.LoginRequest
import com.appstore.data.domain.model.login.LoginResponse
import com.appstore.data.domain.repository.CustomerRepository
import com.appstore.shared.utils.RequestState
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val token: String? = null,
    val requestState: RequestState<LoginResponse> = RequestState.Idle

) {
    val isLoginEnabled: Boolean
        get() = username.isNotBlank()
                && password.isNotBlank()
                && !requestState.isLoading()
}

class AuthenticationViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(value: String) {
        uiState = uiState.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun login() {

        viewModelScope.launch {
            uiState = uiState.copy(
                requestState = RequestState.Loading
            )
            val result = customerRepository.login(
                LoginRequest(
                    uiState.username,
                    uiState.password
                )
            )
            uiState = uiState.copy(
                requestState = result
            )
        }
    }


}