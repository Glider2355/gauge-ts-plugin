package gauge.execution.runner.event

data class ExecutionEvent(
    var type: String = "",
    var id: String = "",
    var filename: String = "",
    var line: Int? = null,
    var parentId: String = "",
    var name: String = "",
    var message: String = "",
    var notification: GaugeNotification? = null,
    var result: ExecutionResult? = null
) {
    companion object {
        const val SUITE_START = "suiteStart"
        const val SPEC_START = "specStart"
        const val SPEC_END = "specEnd"
        const val SCENARIO_START = "scenarioStart"
        const val SCENARIO_END = "scenarioEnd"
        const val SUITE_END = "suiteEnd"
        const val NOTIFICATION = "notification"
        const val STANDARD_OUTPUT = "out"
        const val FAIL = "fail"
        const val SKIP = "skip"
    }
}
