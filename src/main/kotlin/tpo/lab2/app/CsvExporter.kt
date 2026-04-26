package tpo.lab2.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import tpo.lab2.functions.FunctionModule

object CsvExporter {
    fun export(
        module: FunctionModule,
        from: Double,
        to: Double,
        step: Double,
        eps: Double,
        output: String,
        delimiter: Char = ';'
    ): Path {
        require(step != 0.0) { "step must not be 0" }
        require(eps > 0.0) { "eps must be > 0" }

        if (from < to && step < 0.0) error("step must be positive when from < to")
        if (from > to && step > 0.0) error("step must be negative when from > to")

        val path = Paths.get(output)
        val rows = mutableListOf<String>()
        rows += "x${delimiter}result"

        var x = from
        val limitEps = kotlin.math.abs(step) / 1000.0

        while (withinBounds(x, to, step, limitEps)) {
            val y = module.compute(x, eps)
            rows += "${x}${delimiter}${y}"
            x += step
        }

        Files.createDirectories(path.parent ?: Paths.get("."))
        Files.writeString(path, rows.joinToString(separator = System.lineSeparator()))
        return path
    }

    private fun withinBounds(current: Double, end: Double, step: Double, tolerance: Double): Boolean {
        return if (step > 0.0) {
            current <= end + tolerance
        } else {
            current >= end - tolerance
        }
    }
}
