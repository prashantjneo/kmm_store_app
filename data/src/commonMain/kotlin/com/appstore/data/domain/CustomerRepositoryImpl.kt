package com.appstore.data.domain

import com.appstore.data.data.AuthApi
import com.appstore.data.domain.model.login.LoginRequest
import com.appstore.data.domain.model.login.LoginResponse
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
    /*
        override suspend fun login(
            request: LoginRequest
        ): RequestState<LoginResponse> {

            return try {

                val response = api.login(request)

                when {

                    response.status.isSuccess() -> {

                        val data = response.body<LoginResponse>()
                        RequestState.Success(data)
                    }

                    else -> {

                        val errorText = response.bodyAsText()
                        RequestState.Error(errorText)
                    }
                }

            } catch (e: UnresolvedAddressException) {

                RequestState.Error("No Internet Connection")

            } catch (e: SerializationException) {

                RequestState.Error("Data parsing error")

            } catch (e: IOException) {

                RequestState.Error("Network error occurred please check internet")

            } catch (e: Exception) {

                RequestState.Error(e.message ?: "Unknown error")
            }
        }*/

}