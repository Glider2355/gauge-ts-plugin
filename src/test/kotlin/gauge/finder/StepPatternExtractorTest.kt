package gauge.finder

import com.intellij.lang.javascript.psi.JSArrayLiteralExpression
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StepPatternExtractorTest {

    @Nested
    inner class 単一引数 {
        @Test
        fun `文字列リテラルは1要素のリストになる`() {
            val result = StepPatternExtractor.extract(stringLiteral("StepName"))

            assertEquals(listOf("StepName"), result)
        }

        @Test
        fun `日本語やplaceholderを含む文字列もそのまま抽出される`() {
            val result = StepPatternExtractor.extract(stringLiteral("ManagerAPIの <path> にPOSTリクエストする"))

            assertEquals(listOf("ManagerAPIの <path> にPOSTリクエストする"), result)
        }

        @Test
        fun `文字列以外のリテラル（数値等）は空リストになる`() {
            val result = StepPatternExtractor.extract(nonStringLiteral())

            assertEquals(emptyList<String>(), result)
        }

        @Test
        fun `変数参照や結合式などリテラルでない引数は空リストになる`() {
            val result = StepPatternExtractor.extract(mockk<JSExpression>(relaxed = true))

            assertEquals(emptyList<String>(), result)
        }
    }

    @Nested
    inner class 配列引数 {
        @Test
        fun `配列の全要素が順序どおり展開される`() {
            val array = arrayLiteral(stringLiteral("A"), stringLiteral("B"), stringLiteral("C"))

            val result = StepPatternExtractor.extract(array)

            assertEquals(listOf("A", "B", "C"), result)
        }

        @Test
        fun `空配列は空リストになる`() {
            val result = StepPatternExtractor.extract(arrayLiteral())

            assertEquals(emptyList<String>(), result)
        }

        @Test
        fun `文字列リテラルでない要素はスキップされ他の要素は残る`() {
            val array = arrayLiteral(stringLiteral("valid"), nonStringLiteral())

            val result = StepPatternExtractor.extract(array)

            assertEquals(listOf("valid"), result)
        }
    }

    private fun stringLiteral(value: String): JSLiteralExpression =
        mockk<JSLiteralExpression>(relaxed = true).also {
            every { it.isStringLiteral } returns true
            every { it.stringValue } returns value
        }

    private fun nonStringLiteral(): JSLiteralExpression =
        mockk<JSLiteralExpression>(relaxed = true).also {
            every { it.isStringLiteral } returns false
        }

    private fun arrayLiteral(vararg elements: JSExpression): JSArrayLiteralExpression =
        mockk<JSArrayLiteralExpression>(relaxed = true).also {
            every { it.expressions } returns elements.toList().toTypedArray()
        }
}
