package gauge.finder

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GaugeProjectRootFinderTest {

    @Test
    fun gaugeManifest_isDetected() {
        val manifest = """
            {
              "Language": "ts",
              "Plugins": ["html-report", "screenshot"]
            }
        """.trimIndent()
        assertTrue(GaugeProjectRootFinder.isGaugeManifest(manifest))
    }

    @Test
    fun gaugeManifest_withoutWhitespace_isDetected() {
        assertTrue(GaugeProjectRootFinder.isGaugeManifest("""{"Language":"ts","Plugins":[]}"""))
    }

    @Test
    fun webAppManifest_isNotDetected() {
        val manifest = """{ "name": "My App", "short_name": "App", "start_url": "/", "display": "standalone" }"""
        assertFalse(GaugeProjectRootFinder.isGaugeManifest(manifest))
    }

    @Test
    fun emptyContent_isNotDetected() {
        assertFalse(GaugeProjectRootFinder.isGaugeManifest(""))
    }
}
