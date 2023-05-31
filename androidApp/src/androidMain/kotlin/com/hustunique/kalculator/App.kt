package com.hustunique.kalculator

import android.app.Application
import com.hustunique.kalculator.kmm.shared.cache.AppDatabase
import database.DatabaseUtil
import database.createDriver

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseUtil.init(AppDatabase(createDriver(this.applicationContext)))
    }
}