package tpo.lab2.app

import tpo.lab2.functions.FunctionModule
import tpo.lab2.functions.base.LnSeries
import tpo.lab2.functions.base.SinSeries
import tpo.lab2.functions.log.LogFromLn
import tpo.lab2.functions.system.FunctionSystem
import tpo.lab2.functions.trig.CosFromSin
import tpo.lab2.functions.trig.SecFromCos

class ModuleFactory {
    private val sin = SinSeries()
    private val ln = LnSeries()
    private val cos = CosFromSin(sin)
    private val sec = SecFromCos(cos)
    private val log2 = LogFromLn(2.0, ln)
    private val log5 = LogFromLn(5.0, ln)
    private val system = FunctionSystem(sin, cos, sec, log2, log5)

    fun create(kind: ModuleKind): FunctionModule {
        return when (kind) {
            ModuleKind.SIN -> sin
            ModuleKind.COS -> cos
            ModuleKind.SEC -> sec
            ModuleKind.LN -> ln
            ModuleKind.LOG2 -> log2
            ModuleKind.LOG5 -> log5
            ModuleKind.SYSTEM -> system
        }
    }
}
