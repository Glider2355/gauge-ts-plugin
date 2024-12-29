package gauge.execution

import com.intellij.execution.configurations.GeneralCommandLine
import gauge.GaugeConstants

class CommandLineBuilder() {

    fun buildCommandLine(
        commandLine: GeneralCommandLine,
        specs: String?,
        environment: String?,
        tags: String?,
        parallelNodes: Int,
        rowsRange: String?
    ): GeneralCommandLine {
        // 基本コマンドの追加 (gauge run)
        commandLine.addParameter(GaugeConstants.RUN)
        commandLine.addParameter(GaugeConstants.MACHINE_READABLE)
        commandLine.addParameter(GaugeConstants.HIDE_SUGGESTION)

        // tagsが指定されている場合、--tagsフラグとその値を追加
        if (!tags.isNullOrBlank()) {
            commandLine.addParameter(GaugeConstants.TAGS)
            commandLine.addParameter(tags)
        }

        // 環境が指定されている場合、--eフラグとその値を追加
        if (!environment.isNullOrBlank()) {
            commandLine.addParameters(GaugeConstants.ENV_FLAG, environment)
        }

        // テーブルの行の範囲の設定
        if (!rowsRange.isNullOrBlank()) {
            commandLine.addParameter(GaugeConstants.TABLE_ROWS)
            commandLine.addParameter(rowsRange)
        }

        // 並列実行の設定
        if (parallelNodes > 1) {
            commandLine.addParameter(GaugeConstants.PARALLEL)
            commandLine.addParameter("${GaugeConstants.PARALLEL_NODES}=$parallelNodes")
        }

        // 実行する仕様、シナリオの指定
        specs?.split(GaugeConstants.SPEC_FILE_DELIMITER_REGEX)?.forEach { specName ->
            if (specName.isNotEmpty()) {
                commandLine.addParameter(specName.trim())
            }
        }

        return commandLine
    }
}
