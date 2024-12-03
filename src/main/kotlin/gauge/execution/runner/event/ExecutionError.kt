package gauge.execution.runner.event

data class ExecutionError(
    var text: String? = null,
    var filename: String? = null,
    var lineNo: String? = null,
    var message: String? = null,
    var stackTrace: String? = null
) {
    fun format(status: String): String {
        return buildString {
            append(formatText(text, status, "\n"))
            append(formatText(getFileNameWithLineNo(), "Filename: ", "\n"))
            append(formatText(message, "Message: ", "\n"))
            append(formatText(stackTrace, "Stack Trace:\n", ""))
        }
    }

    private fun getFileNameWithLineNo(): String {
        return if (lineNo.isNullOrEmpty()) filename.orEmpty() else "$filename:$lineNo"
    }

    private fun formatText(text: String?, prefix: String, suffix: String): String {
        return if (!text.isNullOrEmpty()) "$prefix$text$suffix" else ""
    }
}
