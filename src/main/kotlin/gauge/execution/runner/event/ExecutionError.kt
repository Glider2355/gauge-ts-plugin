package gauge.execution.runner.event

class ExecutionError {
    var text: String? = null
    var filename: String? = null
    var lineNo: String? = null
    var message: String? = null
    var stackTrace: String? = null

    fun format(status: String?): String {
        return format(this.text, status, "\n") +
                format(fileNameWithLineNo, "Filename: ", "\n") +
                format(this.message, "Message: ", "\n") +
                format(this.stackTrace, "Stack Trace:\n", "")
    }

    private val fileNameWithLineNo: String?
        get() = if (lineNo!!.isEmpty()) filename else format(":", filename, lineNo)

    companion object {
        private fun format(text: String?, prefix: String?, suffix: String?): String {
            return if (!text.isNullOrEmpty()) prefix + text + suffix else ""
        }
    }
}