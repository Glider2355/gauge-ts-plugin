package gauge.execution.runner.event

/**
 * status: (pass, fail, skip)
 * time: 実行時間(ms)
 * errors: エラーオブジェクトの配列 (エラーがない場合は null)
 * beforeHookFailure: 前処理 (Before Hook) の失敗情報 (失敗していない場合は null)
 * afterHookFailure: 後処理 (After Hook) の失敗情報 (失敗していない場合は null)
 * table: テーブル情報 (テーブルがない場合は null)
 */
data class ExecutionResult(
    val status: String,
    val time: Int?,
    val out: String?,
    val errors: Array<ExecutionError>?,
    val beforeHookFailure: ExecutionError?,
    val afterHookFailure: ExecutionError?,
    val table: TableInfo?
) {
    fun skipped(): Boolean {
        return status.equals(ExecutionEvent.SKIP, ignoreCase = true)
    }

    fun failed(): Boolean {
        return status.equals(ExecutionEvent.FAIL, ignoreCase = true)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExecutionResult

        if (time != other.time) return false
        if (status != other.status) return false
        if (out != other.out) return false
        if (errors != null) {
            if (other.errors == null) return false
            if (!errors.contentEquals(other.errors)) return false
        } else if (other.errors != null) return false
        if (beforeHookFailure != other.beforeHookFailure) return false
        if (afterHookFailure != other.afterHookFailure) return false
        if (table != other.table) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time ?: 0
        result = 31 * result + status.hashCode()
        result = 31 * result + (out?.hashCode() ?: 0)
        result = 31 * result + (errors?.contentHashCode() ?: 0)
        result = 31 * result + (beforeHookFailure?.hashCode() ?: 0)
        result = 31 * result + (afterHookFailure?.hashCode() ?: 0)
        result = 31 * result + (table?.hashCode() ?: 0)
        return result
    }
}
