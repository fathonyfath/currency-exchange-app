package dev.fathony.currencyexchange.android

import android.app.Application
import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.db.createDatabase
import dev.fathony.currencyexchange.sqldelight.Database

class MyApplication : Application() {

    private lateinit var database: Database

    override fun onCreate() {
        super.onCreate()

        val driverFactory = DriverFactory(this)
        database = createDatabase(driverFactory)
    }

    fun getDatabase(): Database {
        return database
    }
}