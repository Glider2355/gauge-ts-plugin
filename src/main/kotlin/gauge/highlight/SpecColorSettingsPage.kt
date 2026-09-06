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

    // デモテキストは .spec としてレキサーでハイライトされるだけなので、アノテータ由来の CONCEPT_STEP と
    // .cpt 専用の CONCEPT_HEADING はタグで範囲を指定して重ねる (プレビューではタグ名だけが解釈され、表示からは除かれる)
    private val additionalTags: MutableMap<String, TextAttributesKey> = mutableMapOf(
        "concept_step" to SpecHighlighterColors.CONCEPT_STEP,
        "concept_heading" to SpecHighlighterColors.CONCEPT_HEADING,
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
        <concept_step>* login as "alice"</concept_step>
        * click the login button

        The concept referenced above, as defined in a .cpt file:

        <concept_heading># login as <user></concept_heading>
        * enter <user> into the username field
        * click the login button
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> = additionalTags

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "GaugeTS"
}
