package com.appstore.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.appstore.data.domain.model.login.LoginRequest
import com.appstore.data.domain.model.login.LoginResponse
import com.appstore.data.domain.repository.CustomerRepository
import com.appstore.shared.utils.RequestState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var uiState by mutableStateOf(LoginUiState())
        private set

    var loginRequestState by mutableStateOf<RequestState<LoginResponse>>(
        RequestState.Idle
    )
        private set

    fun onUsernameChange(value: String) {
        uiState = uiState.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun login() {

        viewModelScope.launch {

            loginRequestState = RequestState.Loading

            val result = customerRepository.login(
                LoginRequest(
                    uiState.username,
                    uiState.password
                )
            )

            loginRequestState = result
        }
    }
}