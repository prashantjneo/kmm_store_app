package com.assignment.kmm_product_listing

import androidx.compose.ui.window.ComposeUIViewController
import com.appstore.di.intializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = {intializeKoin()}
) { App() }