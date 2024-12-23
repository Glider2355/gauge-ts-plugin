package gauge.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "GaugeTSPluginSettings", storages = [Storage("GaugeTSPluginSettings.xml")])
class PluginSettings : PersistentStateComponent<PluginSettings> {
    var searchDirectories: MutableList<String> = mutableListOf()
    var validDirectories: MutableList<String> = mutableListOf()
    var gaugeBinaryPath: String? = null
    var gaugeHomePath: String? = null

    override fun getState(): PluginSettings {
        return this
    }

    override fun loadState(state: PluginSettings) {
        searchDirectories = state.searchDirectories
        validDirectories = state.validDirectories
        gaugeBinaryPath = state.gaugeBinaryPath
        gaugeHomePath = state.gaugeHomePath
    }
}
