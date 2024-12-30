package gauge.finder.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StepTextProcessorTest {

    // ---------------------
    // fixStepText メソッドのテスト
    // ---------------------

    @Test
    fun `Stepが複数ある場合、1番目のStepが採用される`() {
        val input = "Given some step, additional text, more text"
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should remove text after the first comma")
    }

    @Test
    fun `ダブルクウォートは削除される`() {
        val input = "\"Given some step\""
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should remove double quotes")
    }

    @Test
    fun `シングルクウォートは削除される`() {
        val input = "'Given some step'"
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should remove single quotes")
    }

    @Test
    fun `ブラケットは削除される`() {
        val input = "[Given some step]"
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should remove brackets")
    }

    @Test
    fun `改行コードは削除される`() {
        val input = "Given some step\n"
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should remove newline characters")
    }

    @Test
    fun `削除対象の記号が複数ある場合`() {
        val input = "\"Given some step, with comma\", [and more], 'and single quotes'\n"
        val expected = "Given some step"
        val actual = StepTextProcessor.fixStepText(input)
        assertEquals(expected, actual, "fixStepText should handle complex input correctly")
    }

    // ---------------------
    // isStepMatch メソッドのテスト
    // ---------------------

    @Test
    fun `TypeScriptとSpecファイルのStep名が一致する場合、trueが返る`() {
        val stepAnnotationText = "ステータスコードが <code> と一致すること"
        val stepText = "ステータスコードが \"200\" と一致すること"
        val result = StepTextProcessor.isStepMatch(stepAnnotationText, stepText)
        assertTrue(result)
    }

    @Test
    fun `TypeScriptとSpecファイルのStep名が一致しない場合、falseが返る`() {
        val stepAnnotationText = "<url> にGETリクエストする"
        val stepText = "ステータスコードが \"200\" と一致すること"
        val result = StepTextProcessor.isStepMatch(stepAnnotationText, stepText)
        assertFalse(result)
    }
}
