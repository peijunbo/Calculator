package ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toTextFieldValue(
    selection: TextRange = TextRange(this.length)
)  = TextFieldValue(this, selection = selection)
fun TextFieldValue.concatKey(key: Key): TextFieldValue {
    println("$key ${this.selection.min} -- ${this.selection.max}")
    val left = this.selection.min
    val right = this.selection.max
    var index: Int = right
    val newText: String = when (key) {
        is Key.Dot -> {
            var newString = "."
            if (left > 0) {
                // cannot input dot if there is no number prefix
                val prefix = this.text[left - 1]
                if (!prefix.isDigit()) {
                    newString = ""
                }
            }
            index += newString.length
            this.text.replaceRange(left, right, newString)


        }

        is Key.NumberKey -> {
            index += key.string.length
            this.text.replaceRange(left, right, key.string)
        }

        is Key.OperatorKey -> {
            if (key is Key.BinaryOperatorKey && left > 0) {
                // if prefix is in "+-*/", replace it
                val prefix = this.text[left - 1].toString()
                if (
                    prefix == Key.Plus.string
                    || prefix == Key.Minus.string
                    || prefix == Key.Multiply.string
                    || prefix == Key.Division.string
                    || prefix == Key.Power.string
                ) {
                    index += key.string.length - 1
                    this.text.replaceRange(left - 1, right, key.string)
                } else {
                    index += key.string.length
                    this.text.replaceRange(
                        this.selection.min,
                        this.selection.max,
                        key.operator
                    )
                }
            } else if (key is Key.Brackets) {
                index += 1
                this.text.replaceRange(
                    this.selection.min,
                    this.selection.max,
                    "()"
                )
            } else {
                index += key.string.length
                this.text.replaceRange(
                    this.selection.min,
                    this.selection.max,
                    key.operator
                )
            }
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
