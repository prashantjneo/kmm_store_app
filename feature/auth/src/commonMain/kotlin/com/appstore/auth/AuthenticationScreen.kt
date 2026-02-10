package com.appstore.auth

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.appstore.auth.components.CenterSnackbarHost
import com.appstore.auth.components.LoginScreen
import com.appstore.shared.utils.RequestState
import org.koin.compose.viewmodel.koinViewModel

@Composable

fun AuthenticationScreen(
    navigateToHome: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            CenterSnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        val viewModel = koinViewModel<AuthenticationViewModel>()
        val uiState = viewModel.uiState

        LoginScreen(
            username = uiState.username,
            password = uiState.password,
            isLoading = uiState.requestState.isLoading(),
            isLoginEnabled = uiState.isLoginEnabled,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login,
            onSignupClick = navigateToHome
        )

        LaunchedEffect(uiState.requestState) {

            when (val result = uiState.requestState) {
                is RequestState.Error -> {
                    snackbarHostState.showSnackbar(result.message)
                }

                is RequestState.Success -> {
                    navigateToHome()
                }

                else -> Unit
            }
        }


    }
}
