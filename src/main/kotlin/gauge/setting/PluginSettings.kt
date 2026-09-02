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

    // Step 検索モード。AUTO = プロジェクト全体を自動スキャン, MANUAL = 下の Directories 一覧を使う
    // 新規インストールでは AUTO。既存の Directories 設定があるユーザーは loadState で MANUAL 扱い
    var scanMode: ScanMode = ScanMode.AUTO
    // AUTO のとき Gauge プロジェクトルート (manifest.json / .gauge/ を持つディレクトリ) 配下に検索スコープを絞る
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
            scanMode.name,
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
        // scanMode が空 = 保存された XML にフィールドが無い (=このバージョン以前のユーザー)
        // その場合 Directories 設定があれば MANUAL を初期値として採用
        scanMode = ScanMode.fromStored(state.scanMode)
            ?: if (state.searchDirectories.isNotEmpty()) ScanMode.MANUAL else ScanMode.AUTO
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
        // XML 互換のため文字列で保持する。空 = 未保存 (旧バージョンのユーザー)。loadState で解決する
        var scanMode: String = "",
        var useGaugeRootScope: Boolean = true
    )

    enum class ScanMode {
        AUTO, MANUAL;

        companion object {
            /** 保存値から復元する。空や不明な値 (旧バージョン・手編集) は null を返し、呼び出し側で既定値を決める */
            fun fromStored(value: String): ScanMode? = entries.firstOrNull { it.name == value }
        }
    }
}
