package tpo.lab2.functions.trig

import kotlin.math.abs
import tpo.lab2.functions.FunctionModule

class SecFromCos(private val cos: FunctionModule) : FunctionModule {
    override fun compute(x: Double, eps: Double): Double {
        val cosValue = cos.compute(x, eps)
        if (cosValue.isNaN() || abs(cosValue) < eps) return Double.NaN
        return 1.0 / cosValue
    }
}
