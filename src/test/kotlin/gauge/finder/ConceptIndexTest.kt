package gauge.finder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConceptIndexTest {

    @Test
    fun headingText_hashHeading_stripsMarkerAndTrailingLineBreaks() {
        assertEquals("login as <user>", ConceptIndex.headingText("# login as <user>\n\n"))
    }

    @Test
    fun headingText_indentedHashHeadingWithoutSpace() {
        assertEquals("login", ConceptIndex.headingText("   #login\n"))
    }

    @Test
    fun headingText_underlinedHeading_dropsUnderline() {
        assertEquals("login as <user>", ConceptIndex.headingText("login as <user>\n=====\n"))
    }

    @Test
    fun normalize_removesQuotedAndAngleBracketParams() {
        assertEquals("login as", ConceptIndex.normalize("login as \"alice\""))
        assertEquals("login as", ConceptIndex.normalize("login as <user>"))
        assertEquals("plain step", ConceptIndex.normalize("plain step"))
    }

    @Test
    fun normalize_conceptHeadingAndSpecStepAgree() {
        val heading = ConceptIndex.normalize(ConceptIndex.headingText("# login as <user> with role <role>\n"))
        val step = ConceptIndex.normalize("login as \"alice\" with role \"admin\"")
        assertEquals(heading, step)
    }
}
