package com.appstore.data.domain

import com.appstore.data.data.AuthApi
import com.appstore.data.domain.model.login.sigin.LoginRequest
import com.appstore.data.domain.model.login.sigin.LoginResponse
import com.appstore.data.domain.repository.CustomerRepository
import com.appstore.shared.utils.safeApiCall
import io.ktor.client.call.body

class CustomerRepositoryImpl(
    private val api: AuthApi
) : CustomerRepository {

    override suspend fun login(
        request: LoginRequest
    ) = safeApiCall(
        apiCall = { api.login(request) },
        parser = { it.body<LoginResponse>() }
    )

}