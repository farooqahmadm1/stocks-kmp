package com.farooq.stocks

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform