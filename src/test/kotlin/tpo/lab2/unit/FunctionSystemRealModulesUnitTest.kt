package tpo.lab2.unit

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.app.ModuleFactory
import tpo.lab2.app.ModuleKind
import tpo.lab2.functions.FunctionModule

class FunctionSystemRealModulesUnitTest {

    private val system: FunctionModule = ModuleFactory().create(ModuleKind.SYSTEM)

    @Test
    fun `left branch matches expected expression for representative x`() {
        val x = -1.0
        val actual = system.compute(x, 1e-6)

        val sec = 1.0 / cos(x)
        val expected = ((((cos(x) * sec) * sin(x)) * sec) * cos(x)).pow(2.0)

        assertEquals(expected, actual, 1e-5)
    }

    @Test
    fun `left branch returns NaN at sec singularity`() {
        val actual = system.compute(-PI / 2.0, 1e-6)
        assertTrue(actual.isNaN())
    }

    @Test
    fun `right branch matches expected expression for representative x`() {
        val x = 2.0
        val actual = system.compute(x, 1e-6)

        val log2X = log2(x)
        val log5X = log(x, 5.0)
        val expected = ((log2X + log5X + log2X).pow(3.0)).pow(2.0) / log5X

        assertEquals(expected, actual, 1e-2)
    }

    @Test
    fun `right branch returns NaN at x equals one`() {
        val actual = system.compute(1.0, 1e-6)
        assertTrue(actual.isNaN())
    }
}
