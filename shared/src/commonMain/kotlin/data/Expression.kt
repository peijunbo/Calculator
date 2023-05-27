package data

/**
 * An Expression Object is like a monomial or a polynomial that has a specific value
 */
interface Expression {

    /**
     * calculate the value of an expression
     */
    fun calculate(): Float

}