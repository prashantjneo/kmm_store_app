package com.appstore.auth

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.appstore.auth.components.CenterSnackbarHost
import com.appstore.auth.components.LoginScreen
import com.appstore.shared.utils.RequestState
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable

fun AuthenticationScreen(
    navigateToHome: () -> Unit
) {
    var loadingStatus by remember { mutableStateOf(false) }
    val messageBarState = rememberMessageBarState()
    val snackbarHostState = remember { SnackbarHostState() }



    Scaffold(
        snackbarHost = {
            CenterSnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        val viewModel = koinViewModel<AuthenticationViewModel>()


        val uiState = viewModel.uiState

        val requestState = viewModel.loginRequestState

        LoginScreen(
            username = uiState.username,
            password = uiState.password,
            isLoading = requestState.isLoading(),
            isLoginEnabled = uiState.isLoginEnabled,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login,
            onSignupClick = navigateToHome
        )

        LaunchedEffect(requestState) {

            when (val result = requestState) {

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
