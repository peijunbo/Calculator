package database

import com.hustunique.kalculator.kmm.shared.cache.DatabaseDriverFactory

class HistorySDK(databaseDriverFactory: DatabaseDriverFactory) {
    val database = Database(databaseDriverFactory)
}