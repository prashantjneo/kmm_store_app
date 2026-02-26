package com.appstore.database

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AndroidContextProvider : KoinComponent {
    val context: Context = get()
}

actual fun createDriverFactory(): DatabaseDriverFactory {
    val context = AndroidContextProvider().context
    return AndroidDriverFactory(context)
}