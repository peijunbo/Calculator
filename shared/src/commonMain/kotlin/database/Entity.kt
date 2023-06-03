package database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class History(
    val id: Long,
    val expression: String,
    val result: String,
    val date: Long
)