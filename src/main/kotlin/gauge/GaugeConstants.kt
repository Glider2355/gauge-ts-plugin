package gauge

object GaugeConstants {
    const val GAUGE: String = "gauge"
    const val RUN: String = "run"
    const val MACHINE_READABLE: String = "--machine-readable"
    const val TAGS: String = "--tags"
    const val PARALLEL: String = "--parallel"
    const val PARALLEL_NODES: String = "-n"
    const val TABLE_ROWS: String = "--table-rows"
    const val ENV_FLAG: String = "--env"
    const val FAILED: String = "--failed"
    const val GAUGE_HOME: String = "GAUGE_HOME"
    const val GAUGE_CUSTOM_CLASSPATH: String = "gauge_custom_classpath"
    const val SPEC_FILE_DELIMITER: String = "||"
    const val SPEC_FILE_DELIMITER_REGEX: String = "\\|\\|"
    private const val COLON = ":"
    const val SPEC_SCENARIO_DELIMITER: String = COLON
    const val HIDE_SUGGESTION: String = "--hide-suggestion"
}