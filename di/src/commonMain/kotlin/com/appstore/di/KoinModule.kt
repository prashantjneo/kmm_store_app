package com.appstore.di


import com.appstore.auth.AuthenticationViewModel
import com.appstore.data.data.AuthApi
import com.appstore.data.domain.CustomerRepositoryImpl
import com.appstore.data.domain.repository.CustomerRepository
import com.appstore.data.remote.createHttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {

    single { createHttpClient() }
    single { AuthApi(get()) }
    single<CustomerRepository> { CustomerRepositoryImpl(get()) }
    viewModelOf(::AuthenticationViewModel)

}


fun intializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {

    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}