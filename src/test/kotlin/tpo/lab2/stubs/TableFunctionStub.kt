package tpo.lab2.stubs

import kotlin.math.abs
import tpo.lab2.functions.FunctionModule

class TableFunctionStub(
    private val table: Map<Double, Double>,
    private val tolerance: Double = 1e-9
) : FunctionModule {

    override fun compute(x: Double, eps: Double): Double {
        val found = table.entries.firstOrNull { abs(it.key - x) <= tolerance }
            ?: error("No stub value configured for x=$x")
        return found.value
    }
}
