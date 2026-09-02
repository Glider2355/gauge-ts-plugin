package gauge.lexer

import com.intellij.psi.tree.IElementType
import gauge.language.token.SpecTokenTypes.SCENARIO_HEADING
import gauge.language.token.SpecTokenTypes.SPEC_HEADING
import gauge.language.token.SpecTokenTypes.STEP
import gauge.language.token.SpecTokenTypes.TAG
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SpecLexerTest {

    private fun tokenTypes(text: String): List<IElementType> {
        val lexer = SpecLexer()
        lexer.start(text)
        val types = mutableListOf<IElementType>()
        while (true) {
            val type = lexer.tokenType ?: break
            types.add(type)
            lexer.advance()
        }
        return types
    }

    @Test
    fun tagsLine_isTokenizedAsTag_caseInsensitiveAndIndented() {
        val text = "# Spec\ntags: smoke, regression\n## Scenario\nTags: a\n  tags: indented\n* step\n"
        assertEquals(listOf(SPEC_HEADING, TAG, SCENARIO_HEADING, TAG, TAG, STEP), tokenTypes(text))
    }

    @Test
    fun tagsInsideStepText_isNotATag() {
        assertEquals(listOf(STEP), tokenTypes("* tags in step text: not a tag\n"))
    }

    @Test
    fun conceptFile_multipleHeadings_areAllSpecHeadings() {
        val text = "# concept one <p>\n* step a\n\n# concept two\n* step b\n"
        assertEquals(listOf(SPEC_HEADING, STEP, SPEC_HEADING, STEP), tokenTypes(text))
    }
}
