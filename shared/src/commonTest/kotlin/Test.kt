import data.Expression
import data.MathOperator
import data.evaluateInfixExpression
import kotlin.test.Test
import kotlin.test.assertTrue

class Test {
    @Test
    fun t() {
        println(evaluateInfixExpression("√(9)/3"))
        println(evaluateInfixExpression("√((10%2)/3+4)"))

    }
}