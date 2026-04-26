package tpo.lab2.integration

import java.nio.file.Files
import kotlin.io.path.createTempDirectory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import tpo.lab2.app.CsvExporter
import tpo.lab2.functions.FunctionModule

@ExtendWith(MockitoExtension::class)
class CsvExporterIntegrationTest {

    @Mock
    private lateinit var module: FunctionModule

    @Test
    fun `exports selected module values to csv with custom step`() {
        whenever(module.compute(0.0, 1e-6)).thenReturn(0.0)
        whenever(module.compute(0.5, 1e-6)).thenReturn(1.0)
        whenever(module.compute(1.0, 1e-6)).thenReturn(2.0)

        val tempDir = createTempDirectory("csv-export-test")
        val output = tempDir.resolve("out.csv")

        CsvExporter.export(
            module = module,
            from = 0.0,
            to = 1.0,
            step = 0.5,
            eps = 1e-6,
            output = output.toString(),
            delimiter = ';'
        )

        val lines = Files.readAllLines(output)
        assertEquals("x;result", lines[0])
        assertEquals("0.0;0.0", lines[1])
        assertEquals("0.5;1.0", lines[2])
        assertEquals("1.0;2.0", lines[3])

        verify(module, times(1)).compute(0.0, 1e-6)
        verify(module, times(1)).compute(0.5, 1e-6)
        verify(module, times(1)).compute(1.0, 1e-6)
    }

    @Test
    fun `fails on zero step`() {
        val tempDir = createTempDirectory("csv-export-test")
        val output = tempDir.resolve("out.csv")

        val ex = kotlin.runCatching {
            CsvExporter.export(
                module = module,
                from = 0.0,
                to = 1.0,
                step = 0.0,
                eps = 1e-6,
                output = output.toString(),
                delimiter = ';'
            )
        }.exceptionOrNull()

        assertTrue(ex is IllegalArgumentException)
        verifyNoInteractions(module)
    }
}
