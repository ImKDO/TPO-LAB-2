package tpo.lab2.integration

import kotlin.math.pow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.functions.system.FunctionSystem
import tpo.lab2.stubs.CosStub
import tpo.lab2.stubs.Log2Stub
import tpo.lab2.stubs.Log5Stub
import tpo.lab2.stubs.SecStub
import tpo.lab2.stubs.SinStub

class FunctionSystemIntegrationTest {

    @Test
    fun `calculates left branch for x lte 0`() {
        val x = -2.0
        val sinStub = SinStub(mapOf(x to -0.5))
        val cosStub = CosStub(mapOf(x to -0.4))
        val secStub = SecStub(mapOf(x to -2.5))
        val log2Stub = Log2Stub(mapOf(2.0 to 1.0))
        val log5Stub = Log5Stub(mapOf(2.0 to 0.43067655807339306))

        val system = FunctionSystem(sinStub, cosStub, secStub, log2Stub, log5Stub)

        assertEquals(0.25, system.compute(x, 1e-6), 1e-12)
    }

    @Test
    fun `returns NaN for left branch at sec singularity`() {
        val x = -1.0
        val sinStub = SinStub(mapOf(x to 0.0))
        val cosStub = CosStub(mapOf(x to 0.0))
        val secStub = SecStub(mapOf(x to Double.NaN))
        val log2Stub = Log2Stub(mapOf(2.0 to 1.0))
        val log5Stub = Log5Stub(mapOf(2.0 to 0.43067655807339306))

        val system = FunctionSystem(sinStub, cosStub, secStub, log2Stub, log5Stub)

        assertTrue(system.compute(x, 1e-6).isNaN())
    }

    @Test
    fun `calculates right branch for x gt 0`() {
        val x = 2.0
        val log2Value = 1.0
        val log5Value = 0.43067655807339306
        val sinStub = SinStub(mapOf(-1.0 to 0.0))
        val cosStub = CosStub(mapOf(-1.0 to 1.0))
        val secStub = SecStub(mapOf(-1.0 to 1.0))
        val log2Stub = Log2Stub(mapOf(x to log2Value))
        val log5Stub = Log5Stub(mapOf(x to log5Value))

        val system = FunctionSystem(sinStub, cosStub, secStub, log2Stub, log5Stub)
        val expected = ((log2Value + log5Value + log2Value).pow(3.0)).pow(2.0) / log5Value

        assertEquals(expected, system.compute(x, 1e-6), 1e-12)
    }

    @Test
    fun `returns NaN for right branch at x equals 1 because log5 is zero`() {
        val x = 1.0
        val sinStub = SinStub(mapOf(-1.0 to 0.0))
        val cosStub = CosStub(mapOf(-1.0 to 1.0))
        val secStub = SecStub(mapOf(-1.0 to 1.0))
        val log2Stub = Log2Stub(mapOf(x to 0.0))
        val log5Stub = Log5Stub(mapOf(x to 0.0))

        val system = FunctionSystem(sinStub, cosStub, secStub, log2Stub, log5Stub)

        assertTrue(system.compute(x, 1e-6).isNaN())
    }
}
