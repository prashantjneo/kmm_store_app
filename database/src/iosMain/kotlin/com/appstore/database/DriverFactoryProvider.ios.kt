package com.appstore.database

actual fun createDriverFactory(): DatabaseDriverFactory {
    return IOSDriverFactory()
}