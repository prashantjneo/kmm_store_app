package com.assignment.kmm_product_listing

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform