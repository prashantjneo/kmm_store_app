package com.appstore.shared.utils

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun d(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    actual fun e(tag: String, message: String) {
        NSLog("ERROR: $tag: $message")
    }
}