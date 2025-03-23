package gauge.finder

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StepTextProcessorTest {
    @Test
    fun findStepFunction_singleQuotation() {
        val input = "'StepName'"
        val result = StepTextProcessor.fixStepText(input)
        assertEquals("StepName", result)
    }
    @Test
    fun findStepFunction_doubleQuotation() {
        val input = "\"StepName\""
        val result = StepTextProcessor.fixStepText(input)
        assertEquals("StepName", result)
    }
    @Test
    fun findStepFunction_MultipleStepNames() {
        val input = "['StepName1', 'StepName2']"
        val result = StepTextProcessor.fixStepText(input)
        assertEquals("StepName1", result)
    }
    @Test
    fun findStepFunction_MultipleStepNamesWithLineBreaks() {
        val input = "[\n'StepName1',\n 'StepName2'\n]"
        val result = StepTextProcessor.fixStepText(input)
        assertEquals("StepName1", result)
    }
    @Test
    fun isStepMatch_doubleQuotation() {
        val stepAnnotationText = "Get request <path>"
        val stepText = "Get request \"/api/v1\""
        val result = StepTextProcessor.isStepMatch(stepAnnotationText, stepText)
        assertTrue(result)
    }
    @Test
    fun isStepMatch_bracket() {
        val stepAnnotationText = "Get request <path>"
        val stepText = "Get request <url>"
        val result = StepTextProcessor.isStepMatch(stepAnnotationText, stepText)
        assertTrue(result)
    }
}