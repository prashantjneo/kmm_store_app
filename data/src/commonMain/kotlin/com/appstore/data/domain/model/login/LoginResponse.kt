package com.appstore.data.domain.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)