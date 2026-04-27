package tpo.lab2.functions.base

import kotlin.math.PI
import kotlin.math.abs
import tpo.lab2.functions.FunctionModule

class SinSeries : FunctionModule {
    override fun compute(x: Double, eps: Double): Double {
        require(eps > 0.0) { "eps must be > 0" }
        if (!x.isFinite()) return Double.NaN

        val normalized = normalizeToMinusPiPlusPi(x)
        var term = normalized
        var sum = normalized
        var n = 1
        var iteration = 0
        val maxIterations = 100_000

        while (abs(term) > eps && iteration < maxIterations) {
            term *= -normalized * normalized / ((2.0 * n) * (2.0 * n + 1.0))
            sum += term
            n++
            iteration++
        }

        return sum
    }

    private fun normalizeToMinusPiPlusPi(x: Double): Double {
        val twoPi = 2.0 * PI
        var value = x % twoPi
        if (value > PI) value -= twoPi
        if (value < -PI) value += twoPi
        return value
    }
}
