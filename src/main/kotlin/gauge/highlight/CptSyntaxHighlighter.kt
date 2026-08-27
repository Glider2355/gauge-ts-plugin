package gauge.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import gauge.language.token.SpecTokenTypes
import gauge.lexer.SpecLexer

class CptSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = SpecLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            SpecTokenTypes.SPEC_HEADING -> arrayOf(SpecHighlighterColors.CONCEPT_HEADING)
            SpecTokenTypes.SCENARIO_HEADING -> arrayOf(SpecHighlighterColors.SCENARIO_HEADING)
            SpecTokenTypes.STEP -> arrayOf(SpecHighlighterColors.STEP)
            SpecTokenTypes.COMMENT -> arrayOf(SpecHighlighterColors.COMMENT)
            SpecTokenTypes.TABLE_HEADER -> arrayOf(SpecHighlighterColors.TABLE_HEADER)
            SpecTokenTypes.TABLE_ROW -> arrayOf(SpecHighlighterColors.TABLE_ROW)
            SpecTokenTypes.TAG -> arrayOf(SpecHighlighterColors.TAG)
            else -> emptyArray()
        }
    }
}
