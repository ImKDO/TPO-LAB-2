package tpo.lab2.integration

import kotlin.math.PI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tpo.lab2.functions.trig.CosFromSin
import tpo.lab2.stubs.SinStub

class CosFromSinIntegrationTest {

    @Test
    fun `computes cos via sin stub`() {
        val sinStub = SinStub(
            mapOf(
                PI / 2.0 to 1.0,
                0.0 to 0.0,
                PI to 0.0,
                -PI / 2.0 to -1.0
            )
        )

        val cos = CosFromSin(sinStub)

        assertEquals(1.0, cos.compute(0.0, 1e-6), 1e-12)
        assertEquals(0.0, cos.compute(PI / 2.0, 1e-6), 1e-12)
        assertEquals(0.0, cos.compute(-PI / 2.0, 1e-6), 1e-12)
        assertEquals(-1.0, cos.compute(PI, 1e-6), 1e-12)
    }
}
