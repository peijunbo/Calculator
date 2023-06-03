package ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun TextFieldValue.concatKey(key: Key): TextFieldValue {
    println("$key ${this.selection.min} -- ${this.selection.max}")
    var index: Int = this.selection.max
    val newText: String = when (key) {
        is Key.NumberKey -> {
            index += key.number.length
            this.text.replaceRange(
                this.selection.min,
                this.selection.max,
                key.number
            )
        }

        is Key.OperatorKey -> {
            index += key.operator.length
            this.text.replaceRange(
                this.selection.min,
                this.selection.max,
                key.operator
            )
        }

        is Key.Delete -> {

            if (this.selection.length > 0) {
                index -= this.selection.length
                this.text.removeRange(
                    this.selection.min,
                    this.selection.max
                )
            } else {
                index = if (this.selection.start > 0) this.selection.start - 1 else 0
                this.text.removeRange(
                    index,
                    this.selection.max
                )
            }
        }

        else -> {
            ""
        }
    }

    return TextFieldValue(
        text = newText,
        selection = TextRange(index, index)
    )
}

fun dateLongToString(date: Long): String =
    Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

fun getDateLong(): Long =
    Clock.System.now().toEpochMilliseconds()
