package data

import kotlin.math.exp
import kotlin.math.pow


enum class MathOperator(val text: String) {
    Plus("+"),
    Minus("-"),
    Multiply("*"),
    Division("/"),
    Root("√"),
    Power("^"),
    Remainder("%"),
    LeftParenthesis("("),
    RightParenthesis(")");

    companion object {
        private val mEntries = MathOperator.values().toList()
        fun isOperator(char: Char) = isOperator(char.toString())
        fun isOperator(text: String) = mEntries.any { it.text == text }
        fun isUnary(char: Char) = isUnary(char.toString())
        fun isUnary(text: String): Boolean {
            return text == Root.text
        }

        fun isRightParenthesis(operator: Char) = isRightParenthesis(operator.toString())
        fun isLeftParenthesis(operator: Char) = isLeftParenthesis(operator.toString())

        fun isRightParenthesis(text: String) = text == RightParenthesis.text
        fun isLeftParenthesis(text: String) = text == LeftParenthesis.text


        fun getPriority(char: Char) = getPriority(char.toString())

        fun getPriority(text: String): Int = when (text) {
            Plus.text, Minus.text -> 1
            Multiply.text, Division.text, Remainder.text -> 2
            Root.text -> 3
            LeftParenthesis.text -> -1
            RightParenthesis.text -> 0
            else -> 0

        }

        fun evaluate(operator: Char, operand1: Float, operand2: Float) =
            evaluate(operator.toString(), operand1, operand2)

        fun evaluate(operator: String, operand1: Float, operand2: Float) = when (operator) {
            Plus.text -> operand1 + operand2
            Minus.text -> operand1 - operand2
            Multiply.text -> operand1 * operand2
            Division.text -> operand1 / operand2
            Remainder.text -> operand1 % operand2
            else -> throw IllegalArgumentException("Invalid operator")
        }

        fun evaluate(operator: Char, operand: Float) = evaluate(operator.toString(), operand)

        fun evaluate(operator: String, operand: Float) = when (operator) {
            Root.text -> operand.pow(0.5f)
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }


}
