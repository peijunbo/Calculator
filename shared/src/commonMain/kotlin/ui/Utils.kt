package ui

inline fun concatKey(expression: String, key: Key): String = when (key) {
    is Key.NumberKey -> {
        expression + key.number
    }

    is Key.OperatorKey -> {
        expression + key.operator
    }

    is Key.ActionKey -> {
        expression
    }
}
