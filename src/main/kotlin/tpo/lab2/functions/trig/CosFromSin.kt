package tpo.lab2.functions.trig

import kotlin.math.PI
import tpo.lab2.functions.FunctionModule

class CosFromSin(private val sin: FunctionModule) : FunctionModule {
    override fun compute(x: Double, eps: Double): Double = sin.compute(PI / 2.0 - x, eps)
}
