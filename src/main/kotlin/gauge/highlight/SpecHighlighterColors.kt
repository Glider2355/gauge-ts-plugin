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

    val CONCEPT_HEADING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_CONCEPT_HEADING",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    val CONCEPT_STEP: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_CONCEPT_STEP",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    // 既定色は colorSchemes/GaugeTS*.xml (additionalTextAttributes) で Default / Darcula 別に与える。
    // それ以外のスキームでは METADATA (アノテーション色) にフォールバックする
    val TAG: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "GAUGE_TAG",
        DefaultLanguageHighlighterColors.METADATA
    )
}
