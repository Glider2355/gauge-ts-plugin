package gauge.execution.runner.event

/**
 * type: このイベントの種類
 * id: イベントの一意の識別子
 * filename: イベントが発生したファイル名
 * line: イベントが発生した行番号
 * parentId: 親イベントの識別子(ルートイベントの場合はnull)
 * name: イベントの名前(スペック名、シナリオ名など)
 * message: イベントに関連付けられたメッセージ
 * notification: 通知情報
 * result: 実行結果
 */
data class ExecutionEvent(
    val type: String,
    val id: String?,
    val filename: String?,
    val line: Int?,
    val parentId: String?,
    val name: String?,
    val message: String?,
    val notification: GaugeNotification?,
    val result: ExecutionResult?
) {
    companion object {
        const val SUITE_START = "suiteStart"
        const val SPEC_START = "specStart"
        const val SPEC_END = "specEnd"
        const val SCENARIO_START = "scenarioStart"
        const val SCENARIO_END = "scenarioEnd"
        const val SUITE_END = "suiteEnd"
        const val NOTIFICATION = "notification"
        const val STANDARD_OUTPUT = "out"
        const val PASS = "pass"
        const val FAIL = "fail"
        const val SKIP = "skip"
    }
}
