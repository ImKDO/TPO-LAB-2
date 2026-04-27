package tpo.lab2.functions.system

import kotlin.math.abs
import kotlin.math.pow
import tpo.lab2.functions.FunctionModule

class FunctionSystem(
    private val sin: FunctionModule,
    private val cos: FunctionModule,
    private val sec: FunctionModule,
    private val log2: FunctionModule,
    private val log5: FunctionModule
) : FunctionModule {

    override fun compute(x: Double, eps: Double): Double {
        return if (x <= 0.0) computeForNonPositiveX(x, eps) else computeForPositiveX(x, eps)
    }

    private fun computeForNonPositiveX(x: Double, eps: Double): Double {
        val cosX1 = cos.compute(x, eps)
        val secX1 = sec.compute(x, eps)
        val sinX = sin.compute(x, eps)
        val secX2 = sec.compute(x, eps)
        val cosX2 = cos.compute(x, eps)

        if (cosX1.isNaN() || secX1.isNaN() || sinX.isNaN() || secX2.isNaN() || cosX2.isNaN()) {
            return Double.NaN
        }

        val branch = ((((cosX1 * secX1) * sinX) * secX2) * cosX2)
        return branch * branch
    }

    private fun computeForPositiveX(x: Double, eps: Double): Double {
        val log2X1 = log2.compute(x, eps)
        val log5X = log5.compute(x, eps)
        val log2X2 = log2.compute(x, eps)

        if (log2X1.isNaN() || log5X.isNaN() || log2X2.isNaN() || abs(log5X) < eps) {
            return Double.NaN
        }

        val numerator = ((log2X1 + log5X + log2X2).pow(3.0)).pow(2.0)
        return numerator / log5X
    }
}
