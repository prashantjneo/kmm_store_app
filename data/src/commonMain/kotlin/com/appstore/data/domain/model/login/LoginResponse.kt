package com.appstore.data.domain.model.login.sigin

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)