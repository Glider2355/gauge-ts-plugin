package gauge.highlight

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import gauge.language.SpecificationIcons
import javax.swing.Icon

class SpecColorSettingsPage : ColorSettingsPage {

    private val descriptors = arrayOf(
        AttributesDescriptor("Spec heading (#)", SpecHighlighterColors.SPEC_HEADING),
        AttributesDescriptor("Scenario heading (##)", SpecHighlighterColors.SCENARIO_HEADING),
        AttributesDescriptor("Step (*)", SpecHighlighterColors.STEP),
        AttributesDescriptor("Comment", SpecHighlighterColors.COMMENT),
        AttributesDescriptor("Table header", SpecHighlighterColors.TABLE_HEADER),
        AttributesDescriptor("Table row", SpecHighlighterColors.TABLE_ROW),
        AttributesDescriptor("Concept heading (# in .cpt)", SpecHighlighterColors.CONCEPT_HEADING),
        AttributesDescriptor("Concept-referenced step (in .spec)", SpecHighlighterColors.CONCEPT_STEP),
        AttributesDescriptor("Tag (tags:)", SpecHighlighterColors.TAG),
    )

    override fun getIcon(): Icon = SpecificationIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = SpecSyntaxHighlighter()

    override fun getDemoText(): String = """
        # Sample specification

        tags: sample, demo

        This is a description block.

        | user | role  |
        |------|-------|
        | Al   | admin |
        | Bo   | guest |

        ## First scenario

        tags: happy-path
        * open the login page
        * enter "alice" into the username field
        * click the login button
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "GaugeTS"
}
