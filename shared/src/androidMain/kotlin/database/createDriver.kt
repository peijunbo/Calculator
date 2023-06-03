package database

import android.content.Context
import com.hustunique.kalculator.kmm.shared.cache.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

fun createDriver(context: Context): SqlDriver = AndroidSqliteDriver(
    schema = AppDatabase.Schema,
    context = context,
    name = "kalculator.db"
)
