package tpo.lab2.app

enum class ModuleKind {
    SIN,
    COS,
    SEC,
    LN,
    LOG2,
    LOG5,
    SYSTEM;

    companion object {
        fun from(value: String): ModuleKind =
            values().firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: error("Unknown module: $value")
    }
}
