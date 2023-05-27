package data

import data.MathOperator.Companion.getPriority
import data.MathOperator.Companion.isUnary
import kotlin.math.exp

/**
 * An Expression Object is like a monomial or a polynomial that has a specific value
 */
interface Expression {

    /**
     * calculate the value of an expression
     */
    fun calculate(): Float

}


/**
 * @return the number and the end offset(exclusive)
 */
fun readNumber(expression: String, start: Int): Pair<Float, Int> {
    var i = start
    while (!expression[i].isDigit()) i++
    val begin = i // the begin of a number
    while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
        i++
    }
    return Pair(expression.substring(begin, i).toFloat(), i)
}

fun evaluateInfixExpression(expression: String): Float {
    val operands = mutableListOf<Float>()
    val operators = mutableListOf<Char>()
    var i = 0

    while (i < expression.length) {
        val char = expression[i]
        if (char.isWhitespace()) {
            i++
            continue
        }

        if (char.isDigit()) {
            //parse a number
            val (num, end) = readNumber(expression, start = i)
            i = end
            operands.add(num)
        } else if (MathOperator.isOperator(char)) {
            if (MathOperator.isLeftParenthesis(char)) {
                operators.add(char)
                i++
                continue
            }
            while (operators.isNotEmpty() && getPriority(operators.last()) >= getPriority(char)) {
                val operator = operators.removeLast()
                if (isUnary(operator)) {
                    operands.add(
                        MathOperator.evaluate(operator, operands.removeLast())
                    )
                } else {
                    val operand2 = operands.removeLast()
                    val operand1 = operands.removeLast()
                    val result = MathOperator.evaluate(operator, operand1, operand2)
                    operands.add(result)
                }
            }
            if (MathOperator.isRightParenthesis(char)) {
                // pop left parenthesis "("
                operators.removeLast()
            } else {
                operators.add(char)
            }
            i++
        } else {
            throw IllegalArgumentException("Invalid character: $char")
        }
    }

    while (operators.isNotEmpty()) {

        val operator = operators.removeLast()
        if (isUnary(operator)) {
            operands.add(
                MathOperator.evaluate(operator, operands.removeLast())
            )
        } else {
            val operand2 = operands.removeLast()
            val operand1 = operands.removeLast()
            val result = MathOperator.evaluate(operator, operand1, operand2)
            operands.add(result)
        }
    }

    return operands.single()
}
