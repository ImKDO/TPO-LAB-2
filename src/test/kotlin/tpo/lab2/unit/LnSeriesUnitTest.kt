package tpo.lab2.unit

import kotlin.math.ln
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.functions.base.LnSeries

class LnSeriesUnitTest {

    private val module = LnSeries()

    @Test
    fun `throws on non-positive eps`() {
        assertFailsWith<IllegalArgumentException> { module.compute(1.0, 0.0) }
        assertFailsWith<IllegalArgumentException> { module.compute(1.0, -1e-6) }
    }

    @Test
    fun `returns NaN for invalid domain`() {
        assertTrue(module.compute(Double.NaN, 1e-6).isNaN())
        assertTrue(module.compute(0.0, 1e-6).isNaN())
        assertTrue(module.compute(-1.0, 1e-6).isNaN())
    }

    @Test
    fun `returns positive infinity for positive infinity`() {
        val actual = module.compute(Double.POSITIVE_INFINITY, 1e-6)
        assertEquals(Double.POSITIVE_INFINITY, actual)
    }

    @Test
    fun `matches reference for representative equivalence points`() {
        val points = listOf(0.25, 0.5, 1.0, 1.5, 2.0, 5.0)

        points.forEach { x ->
            val actual = module.compute(x, 1e-8)
            val expected = ln(x)
            assertEquals(expected, actual, 1e-6, "x=$x")
        }
    }
}
