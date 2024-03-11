package com.farooq.stocks.common.data

// Importing the SqlDriver class from the SqlDelight library
import app.cash.sqldelight.db.SqlDriver

// Declaration of an expect class DatabaseDriverFactory
expect class DatabaseDriverFactory {
    // Abstract function to create a SqlDriver
    fun createDriver(): SqlDriver
}