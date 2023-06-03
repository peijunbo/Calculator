package database

import com.hustunique.kalculator.kmm.shared.cache.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

fun createDriver(): SqlDriver =
    NativeSqliteDriver(AppDatabase.Schema, "kalculator.db")
    
