package tpo.lab2

import tpo.lab2.app.CsvExporter
import tpo.lab2.app.ModuleFactory
import tpo.lab2.app.ModuleKind

fun main(args: Array<String>) {
    if (args.isEmpty() || args.contains("--help")) {
        printUsage()
        return
    }

    val parsed = parseArgs(args)

    val moduleName = parsed["module"] ?: error("--module is required")
    val from = parsed["from"]?.toDoubleOrNull() ?: error("--from is required")
    val to = parsed["to"]?.toDoubleOrNull() ?: error("--to is required")
    val step = parsed["step"]?.toDoubleOrNull() ?: error("--step is required")
    val eps = parsed["eps"]?.toDoubleOrNull() ?: error("--eps is required")
    val out = parsed["out"] ?: error("--out is required")
    val delimiter = parsed["delimiter"]?.singleOrNull() ?: ';'

    val factory = ModuleFactory()
    val module = factory.create(ModuleKind.from(moduleName))
    val outputPath = CsvExporter.export(module, from, to, step, eps, out, delimiter)
    println("CSV generated: $outputPath")
}

private fun parseArgs(args: Array<String>): Map<String, String> {
    val result = mutableMapOf<String, String>()
    var i = 0
    while (i < args.size) {
        val key = args[i]
        if (!key.startsWith("--")) {
            i++
            continue
        }
        if (i + 1 >= args.size) error("Missing value for $key")

        result[key.removePrefix("--")] = args[i + 1]
        i += 2
    }
    return result
}

private fun printUsage() {
    println(
        """
        Usage:
          --module <sin|cos|sec|ln|log2|log5|system>
          --from <number>
          --to <number>
          --step <number>
          --eps <number>
          --out <path to csv>
          [--delimiter <single char>]
        """.trimIndent()
    )
}
