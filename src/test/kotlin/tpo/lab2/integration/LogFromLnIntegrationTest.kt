package tpo.lab2.integration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tpo.lab2.functions.log.LogFromLn
import tpo.lab2.stubs.LnStub

class LogFromLnIntegrationTest {

    @Test
    fun `computes log2 via ln stub`() {
        val lnStub = LnStub(
            mapOf(
                2.0 to 0.6931471805599453,
                4.0 to 1.3862943611198906
            )
        )

        val log2 = LogFromLn(2.0, lnStub)

        assertEquals(2.0, log2.compute(4.0, 1e-6), 1e-12)
    }

    @Test
    fun `returns NaN when ln dependency returns NaN`() {
        val lnStub = LnStub(
            mapOf(
                1.0 to Double.NaN,
                5.0 to 1.6094379124341003
            )
        )

        val log5 = LogFromLn(5.0, lnStub)
        assertTrue(log5.compute(1.0, 1e-6).isNaN())
    }
}
