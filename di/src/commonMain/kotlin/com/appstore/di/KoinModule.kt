package com.appstore.di


import com.appstore.auth.product_details.ProductDetailViewModel
import com.appstore.auth.product_list.ProductListViewModel
import com.appstore.auth.signin.AuthenticationViewModel
import com.appstore.data.data.AuthApi
import com.appstore.data.data.ProductApi
import com.appstore.data.data.ProductLocalDataSource
import com.appstore.data.domain.CustomerRepositoryImpl
import com.appstore.data.domain.ProductRepositoryImpl
import com.appstore.data.domain.repository.CustomerRepository
import com.appstore.data.domain.repository.ProductRepository
import com.appstore.data.remote.createHttpClient
import com.appstore.database.AppDatabase
import com.appstore.database.DatabaseFactory
import com.appstore.database.createDriverFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {

    single { createHttpClient() }
    single { AuthApi(get()) }
    single { ProductApi(get()) }
    single {
        ProductLocalDataSource(get())
    }
    single<CustomerRepository> { CustomerRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(api = get(), local = get()) }
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailViewModel)


}

val databaseModule = module {

    single {
        DatabaseFactory(createDriverFactory()).database
    }

    single {
        get<AppDatabase>().productQueries
    }
}


fun intializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {

    startKoin {
        config?.invoke(this)
        modules(sharedModule,databaseModule)
    }
}