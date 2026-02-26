package com.appstore.database

import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IOSDriverFactory : DatabaseDriverFactory {

    override fun createDriver() =
        NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "store.db"
        )
}