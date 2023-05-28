package ui

sealed interface Key {
    sealed class NumberKey(val number: String) : Key
    sealed class OperatorKey(val operator: String) : Key
    sealed class ActionKey(val action: String) : Key
    object Zero : NumberKey("0")

    object One : NumberKey("1")
    object Two : NumberKey("2")
    object Three : NumberKey("3")
    object Four : NumberKey("4")
    object Five : NumberKey("5")
    object Six : NumberKey("6")
    object Seven : NumberKey("7")
    object Eight : NumberKey("8")
    object Nine : NumberKey("9")
    object Dot : NumberKey(".")
    object Plus : OperatorKey("+")
    object Minus : OperatorKey("-")
    object Multiply : OperatorKey("*")
    object Division : OperatorKey("/")
    object Root : OperatorKey("√")
    object Power : OperatorKey("^")
    object Percent : OperatorKey("%")
    object Equal : ActionKey("=")
    object AC : ActionKey("AC")
    object Clear : OperatorKey("C")
    object Delete : ActionKey("⌫")
    object Brackets : OperatorKey("( )")
}