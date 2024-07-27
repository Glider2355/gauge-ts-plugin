package gauge.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "PluginSettings", storages = [Storage("PluginSettings.xml")])
@Service(Service.Level.PROJECT)
class PluginSettings : PersistentStateComponent<PluginSettings.State> {
    var searchDirectories: MutableList<String> = mutableListOf()
    var validDirectories: MutableList<String> = mutableListOf()

    override fun getState(): State {
        return State(searchDirectories, validDirectories)
    }

    override fun loadState(state: State) {
        searchDirectories = state.searchDirectories.toMutableList()
        validDirectories = state.validDirectories.toMutableList()
    }

    data class State(
        var searchDirectories: List<String> = listOf(),
        var validDirectories: List<String> = listOf()
    )
}