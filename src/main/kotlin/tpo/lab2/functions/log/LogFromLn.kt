package tpo.lab2.functions.log

import kotlin.math.abs
import tpo.lab2.functions.FunctionModule

class LogFromLn(private val base: Double, private val ln: FunctionModule) : FunctionModule {
    init {
        require(base > 0.0 && base != 1.0) { "base must be > 0 and != 1" }
    }

    override fun compute(x: Double, eps: Double): Double {
        val lnX = ln.compute(x, eps)
        val lnBase = ln.compute(base, eps)
        if (lnX.isNaN() || lnBase.isNaN() || abs(lnBase) < eps) return Double.NaN
        return lnX / lnBase
    }
}
