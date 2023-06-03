package database



import com.hustunique.kalculator.kmm.shared.cache.AppDatabase
import com.hustunique.kalculator.kmm.shared.cache.DatabaseDriverFactory

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries
    fun clearAllHistories() {
        dbQuery.removeAllHistories()
    }
    fun insertHistory(history: History) {
        dbQuery.insertHistory(
            null,
            history.expression,
            history.result
        )
    }
    fun selectAllHistories() = dbQuery.selectAllHistories(::mapHistorySelecting).executeAsList()

}

private fun mapHistorySelecting(
    id: Long,
    expression: String,
    result: String
) = History(id, expression, result)
