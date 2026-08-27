package gauge.highlight

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object SpecHighlighterColors {
    val SPEC_HEADING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_SPEC_HEADING",
        DefaultLanguageHighlighterColors.CLASS_NAME
    )
    val SCENARIO_HEADING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_SCENARIO_HEADING",
        DefaultLanguageHighlighterColors.INSTANCE_METHOD
    )
    val STEP: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_STEP",
        DefaultLanguageHighlighterColors.IDENTIFIER
    )
    val COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_COMMENT",
        DefaultLanguageHighlighterColors.LINE_COMMENT
    )
    val TABLE_HEADER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_TABLE_HEADER",
        DefaultLanguageHighlighterColors.STATIC_FIELD
    )
    val TABLE_ROW: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_TABLE_ROW",
        DefaultLanguageHighlighterColors.STRING
    )
}
