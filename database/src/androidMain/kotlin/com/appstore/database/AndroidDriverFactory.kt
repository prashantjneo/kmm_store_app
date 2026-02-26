package com.appstore.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDriverFactory(
    private val context: Context
) : DatabaseDriverFactory {

    override fun createDriver() =
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "store.db"
        )
}