package data


/**
 * Monomial is an [Expression] that contains multiplying multiple [Expression]s such as "2*(3+4)^2*5"
 */
class Monomial : Expression {

    private val items: List<Expression> = mutableListOf()
    override fun calculate(): Float {
        var ret = 0.0F
        items.forEach {
            ret += it.calculate()
        }
        return ret
    }

}

