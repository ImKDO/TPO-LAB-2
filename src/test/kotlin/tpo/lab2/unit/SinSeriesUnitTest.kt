package tpo.lab2.unit

import kotlin.math.PI
import kotlin.math.sin
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.functions.base.SinSeries

class SinSeriesUnitTest {

    private val module = SinSeries()

    @Test
    fun `throws on non-positive eps`() {
        assertFailsWith<IllegalArgumentException> { module.compute(1.0, 0.0) }
        assertFailsWith<IllegalArgumentException> { module.compute(1.0, -1e-6) }
    }

    @Test
    fun `returns NaN for non-finite x`() {
        assertTrue(module.compute(Double.NaN, 1e-6).isNaN())
        assertTrue(module.compute(Double.POSITIVE_INFINITY, 1e-6).isNaN())
        assertTrue(module.compute(Double.NEGATIVE_INFINITY, 1e-6).isNaN())
    }

    @Test
    fun `matches reference for representative equivalence points`() {
        val points = listOf(
            0.0,
            PI / 6.0,
            PI / 2.0,
            -PI / 3.0,
            PI,
            2.75
        )

        points.forEach { x ->
            val actual = module.compute(x, 1e-8)
            val expected = sin(x)
            assertEquals(expected, actual, 1e-6, "x=$x")
        }
    }
}
