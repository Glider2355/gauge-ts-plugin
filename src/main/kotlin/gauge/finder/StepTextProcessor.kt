package gauge.finder

internal object StepTextProcessor {

    fun fixStepText(text: String): String {
        val noComma = text.split(",")[0]
        val noQuotes = noComma.replace("\"", "").replace("'", "")
        val cleaned = noQuotes.replace("[", "").replace("]", "").replace("\n", "")
        return cleaned.trimStart()
    }

    fun isStepMatch(stepAnnotationText: String, stepText: String): Boolean {
        val stepAnnotationTextMatch = stepAnnotationText
            .replace("<[^>]+>".toRegex(), "")
            .trimEnd()
        val stepTextMatch = stepText
            .replace("\"[^\"]*\"".toRegex(), "")
            .replace("<[^>]+>".toRegex(), "")
            .trimEnd()

        return stepAnnotationTextMatch == stepTextMatch
    }
}
