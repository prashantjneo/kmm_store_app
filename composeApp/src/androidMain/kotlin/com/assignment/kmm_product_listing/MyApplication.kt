package com.assignment.kmm_product_listing

import android.app.Application
import com.appstore.di.intializeKoin

import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        intializeKoin(
            config = {
                androidContext(this@MyApplication)
            }
        )

    }
}