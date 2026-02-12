package com.appstore.shared.utils

expect object AppLogger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
}
