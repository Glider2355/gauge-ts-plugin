package gauge.finder

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StepTextProcessorTest {

    @Nested
    inner class isStepMatch_一致 {
        @Test
        fun `annotation側のplaceholderとspec側のダブルクォート引数は同一とみなされる`() {
            val result = StepTextProcessor.isStepMatch(
                stepAnnotationText = "Get request <path>",
                stepText = "Get request \"/api/v1\"",
            )

            assertTrue(result)
        }

        @Test
        fun `annotationとspecの両方でplaceholder名が異なっても一致する`() {
            val result = StepTextProcessor.isStepMatch(
                stepAnnotationText = "Get request <path>",
                stepText = "Get request <url>",
            )

            assertTrue(result)
        }

        @Test
        fun `placeholderを含まないstepでも本文が完全一致なら一致する`() {
            val result = StepTextProcessor.isStepMatch(
                stepAnnotationText = "ログアウトする",
                stepText = "ログアウトする",
            )

            assertTrue(result)
        }
    }

    @Nested
    inner class isStepMatch_不一致 {
        @Test
        fun `placeholderを除いた本文が異なる場合は不一致`() {
            val result = StepTextProcessor.isStepMatch(
                stepAnnotationText = "Get request <path>",
                stepText = "Post request \"/api/v1\"",
            )

            assertFalse(result)
        }

        @Test
        fun `annotation側に余分な単語がある場合は不一致`() {
            val result = StepTextProcessor.isStepMatch(
                stepAnnotationText = "Get request <path> with header",
                stepText = "Get request \"/api/v1\"",
            )

            assertFalse(result)
        }
    }
}
