package tpo.lab2.functions.base

import kotlin.math.abs
import tpo.lab2.functions.FunctionModule

class LnSeries : FunctionModule {
    override fun compute(x: Double, eps: Double): Double {
        require(eps > 0.0) { "eps must be > 0" }
        if (x.isNaN() || x <= 0.0) return Double.NaN
        if (x == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY

        var scaled = x
        var powerOfTwo = 0

        while (scaled > 2.0) {
            scaled /= 2.0
            powerOfTwo++
        }

        while (scaled < 0.5) {
            scaled *= 2.0
            powerOfTwo--
        }

        val z = (scaled - 1.0) / (scaled + 1.0)
        val z2 = z * z

        var numerator = z
        var denominator = 1.0
        var sum = 0.0
        var iteration = 0
        val maxIterations = 100_000

        while (iteration < maxIterations) {
            val addend = numerator / denominator
            sum += addend
            if (abs(addend) < eps) break

            numerator *= z2
            denominator += 2.0
            iteration++
        }

        return 2.0 * sum + powerOfTwo * LN_2
    }

    private companion object {
        const val LN_2 = 0.6931471805599453
    }
}
