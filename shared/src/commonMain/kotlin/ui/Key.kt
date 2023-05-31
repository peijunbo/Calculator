package ui

sealed class Key(val string: String) {

    sealed class NumberKey(val number: String) : Key(number)
    sealed class OperatorKey(val operator: String) : Key(operator)
    sealed class UnaryOperatorKey(operator: String) : OperatorKey(operator)
    sealed class BinaryOperatorKey(operator: String) : OperatorKey(operator)
    sealed class ActionKey(val action: String) : Key(action)
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
    object Plus : BinaryOperatorKey("+")
    object Minus : BinaryOperatorKey("−")
    object Multiply : BinaryOperatorKey("×")
    object Division : BinaryOperatorKey("÷")
    object Root : UnaryOperatorKey("√")
    object Power : BinaryOperatorKey("^")
    object Percent : UnaryOperatorKey("%")
    object Equal : ActionKey("=")
    object AC : ActionKey("AC")
    object Clear : OperatorKey("C")
    object Delete : ActionKey("⌫")
    object Brackets : OperatorKey("()")
}