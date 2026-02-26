package com.appstore.database

class DatabaseFactory(
    driverFactory: DatabaseDriverFactory
) {
    val database = AppDatabase(driverFactory.createDriver())
}