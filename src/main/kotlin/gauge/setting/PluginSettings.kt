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
    var gaugeBinaryPath: String = "/opt/homebrew/bin/gauge"
    var gaugeHomePath: String = "~/.gauge"
    var parallelNode: Int = 3
    var enableEnv = false
    var envValue = ""
    var enableEnvVar = false
    var envVarValue = ""

    // Step 検索モード。"AUTO" = プロジェクト全体を自動スキャン, "MANUAL" = 下の Directories 一覧を使う
    var scanMode: String = ScanMode.MANUAL
    // AUTO のとき .gauge/ を含む親ディレクトリを検索スコープにする
    var useGaugeRootScope: Boolean = true


    override fun getState(): State {
        return State(
            searchDirectories,
            validDirectories,
            gaugeBinaryPath,
            gaugeHomePath,
            parallelNode,
            enableEnv,
            envValue,
            enableEnvVar,
            envVarValue,
            scanMode,
            useGaugeRootScope
        )
    }

    override fun loadState(state: State) {
        searchDirectories = state.searchDirectories.toMutableList()
        validDirectories = state.validDirectories.toMutableList()
        gaugeBinaryPath = state.gaugeBinaryPath
        gaugeHomePath = state.gaugeHomePath
        parallelNode = state.parallelNode
        enableEnv = state.enableEnv
        envValue = state.envValue
        enableEnvVar = state.enableEnvVar
        envVarValue = state.envVarValue
        scanMode = state.scanMode
        useGaugeRootScope = state.useGaugeRootScope
    }

    data class State(
        var searchDirectories: List<String> = listOf(),
        var validDirectories: List<String> = listOf(),
        var gaugeBinaryPath: String = "/opt/homebrew/bin/gauge",
        var gaugeHomePath: String = "~/.gauge",
        var parallelNode: Int = 3,
        var enableEnv: Boolean = false,
        var envValue: String = "",
        var enableEnvVar: Boolean = false,
        var envVarValue: String = "",
        var scanMode: String = ScanMode.MANUAL,
        var useGaugeRootScope: Boolean = true
    )

    object ScanMode {
        const val AUTO = "AUTO"
        const val MANUAL = "MANUAL"
    }
}