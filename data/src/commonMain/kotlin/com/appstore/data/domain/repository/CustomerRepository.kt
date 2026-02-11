package com.appstore.data.domain.repository

import com.appstore.data.domain.model.login.sigin.LoginRequest
import com.appstore.data.domain.model.login.sigin.LoginResponse
import com.appstore.shared.utils.RequestState

interface CustomerRepository {

    suspend fun login(
        request: LoginRequest
    ): RequestState<LoginResponse>
}