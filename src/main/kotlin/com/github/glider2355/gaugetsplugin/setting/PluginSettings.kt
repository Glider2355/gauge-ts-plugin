package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "PluginSettings", storages = [Storage("PluginSettings.xml")])
@Service(Service.Level.PROJECT)
class PluginSettings : PersistentStateComponent<PluginSettings.State> {
    private var myState = State()

    var searchDirectories: MutableList<String>
        get() = myState.searchDirectories
        set(value) {
            myState.searchDirectories = value
        }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }

    data class State(var searchDirectories: MutableList<String> = mutableListOf())
}
