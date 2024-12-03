package gauge.execution.runner.event

data class ExecutionResult(
    var status: String? = null,
    var time: Int? = null,
    var out: String? = null,
    var errors: Array<ExecutionError>? = null,
    var beforeHookFailure: ExecutionError? = null,
    var afterHookFailure: ExecutionError? = null,
    var table: TableInfo? = null
) {
    fun skipped(): Boolean {
        return status.equals(ExecutionEvent.SKIP, ignoreCase = true)
    }

    fun failed(): Boolean {
        return status.equals(ExecutionEvent.FAIL, ignoreCase = true)
    }
}
