/*
 * Copyright (C) 2020 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gauge.execution.runner.event

data class ExecutionResult(
    var status: String = "",
    var time: Int? = null,
    var out: String? = null,
    var errors: Array<ExecutionError>? = null,
    var beforeHookFailure: ExecutionError? = null,
    var afterHookFailure: ExecutionError? = null,
    var table: TableInfo? = null
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
