package tpo.lab2.integration

import kotlin.math.PI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.functions.trig.SecFromCos
import tpo.lab2.stubs.CosStub

class SecFromCosIntegrationTest {

    @Test
    fun `computes sec via cos stub and respects domain`() {
        val cosStub = CosStub(
            mapOf(
                0.0 to 1.0,
                PI to -1.0,
                PI / 2.0 to 0.0,
                -PI / 2.0 to 0.0
            )
        )

        val sec = SecFromCos(cosStub)

        assertEquals(1.0, sec.compute(0.0, 1e-6), 1e-12)
        assertEquals(-1.0, sec.compute(PI, 1e-6), 1e-12)
        assertTrue(sec.compute(PI / 2.0, 1e-6).isNaN())
        assertTrue(sec.compute(-PI / 2.0, 1e-6).isNaN())
    }
}
