package gauge.finder

internal object StepTextProcessor {

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
