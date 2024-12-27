package gauge.execution.runner.event

data class ExecutionError(
    val text: String,
    val filename: String,
    val lineNo: String,
    val message: String,
    val stackTrace: String
){
    fun format(status: String?): String {
        return format(this.text, status, "\n") +
                format(fileNameWithLineNo, "Filename: ", "\n") +
                format(this.message, "Message: ", "\n") +
                format(this.stackTrace, "Stack Trace:\n", "")
    }

    private val fileNameWithLineNo: String
        get() = if (lineNo.isEmpty()) filename else format(":", filename, lineNo)

    companion object {
        private fun format(text: String?, prefix: String?, suffix: String?): String {
            return if (!text.isNullOrEmpty()) prefix + text + suffix else ""
        }
    }
}