package database



import com.hustunique.kalculator.kmm.shared.cache.AppDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
object DatabaseUtil {
    private var database: AppDatabase? = null
    fun init(value: AppDatabase) {
        if (database == null)
            this.database = value

    }

    private val dbQuery by lazy { database!!.appDatabaseQueries }
    fun clearAllHistories() {
        dbQuery.removeAllHistories()
    }

    fun insertHistory(history: History) {
        dbQuery.insertHistory(
            null,
            history.expression,
            history.result,
            history.date
        )
    }

    val histories: Flow<List<History>> by lazy { dbQuery.selectAllHistories(::mapHistorySelecting).asFlow().mapToList() }
    fun selectAllHistories() = dbQuery.selectAllHistories(::mapHistorySelecting).executeAsList()

}

private fun mapHistorySelecting(
    id: Long,
    expression: String,
    result: String,
    date: Long
) = History(id, expression, result, date)
