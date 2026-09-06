package gauge.setting

import gauge.setting.PluginSettings.ScanMode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PluginSettingsTest {

    private fun load(state: PluginSettings.State): PluginSettings =
        PluginSettings().apply { loadState(state) }

    @Test
    fun legacyStateWithDirectories_migratesToManual() {
        val settings = load(PluginSettings.State(searchDirectories = listOf("/steps"), scanMode = ""))
        assertEquals(ScanMode.MANUAL, settings.scanMode)
    }

    @Test
    fun legacyStateWithoutDirectories_defaultsToAuto() {
        val settings = load(PluginSettings.State(scanMode = ""))
        assertEquals(ScanMode.AUTO, settings.scanMode)
    }

    @Test
    fun storedScanMode_isRestored() {
        assertEquals(ScanMode.MANUAL, load(PluginSettings.State(scanMode = "MANUAL")).scanMode)
        assertEquals(ScanMode.AUTO, load(PluginSettings.State(searchDirectories = listOf("/steps"), scanMode = "AUTO")).scanMode)
    }

    @Test
    fun unknownStoredScanMode_fallsBackLikeLegacyState() {
        assertEquals(ScanMode.AUTO, load(PluginSettings.State(scanMode = "BOGUS")).scanMode)
        assertEquals(ScanMode.MANUAL, load(PluginSettings.State(searchDirectories = listOf("/steps"), scanMode = "BOGUS")).scanMode)
    }

    @Test
    fun getState_persistsScanModeAsName() {
        val settings = PluginSettings().apply { scanMode = ScanMode.MANUAL }
        assertEquals("MANUAL", settings.state.scanMode)
    }
}
